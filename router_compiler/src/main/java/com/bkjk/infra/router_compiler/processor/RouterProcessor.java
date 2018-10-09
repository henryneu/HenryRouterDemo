package com.bkjk.infra.router_compiler.processor;

import com.bkjk.infra.router_annotation.Route;
import com.bkjk.infra.router_annotation.modle.RouteMeta;
import com.bkjk.infra.router_compiler.utils.Constant;
import com.bkjk.infra.router_compiler.utils.LogUtils;
import com.bkjk.infra.router_compiler.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Author: zhouzhenhua
 * Date: 2018/9/27
 * Version: 1.0.0
 * Description:
 */
@AutoService(Process.class)
/**
 * 获取每个module的名字,用来生成对应module下存放路由信息的类文件名
 * 处理器接收的参数 替代 {@link AbstractProcessor#getSupportedOptions()} 函数
 */
@SupportedOptions(Constant.ARGUMENTS_NAME)
/**
 * 指定了需要处理的注解的路径地址,在此就是Route.class的路径地址
 * 注册给哪些注解的 替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 */
@SupportedAnnotationTypes(Constant.ANNOTATION_TYPE_ROUTE)
/**
 * 指定使用的Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouterProcessor extends AbstractProcessor {
    /**
     * key:组名 value:类名
     */
    private Map<String, String> mRootMap = new TreeMap<>();

    /**
     * 分组 key:组名 value:对应组的路由信息
     */
    private Map<String, List<RouteMeta>> mGroupMap = new HashMap<>();

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements mElementUtils;

    /**
     * type(类信息)工具类
     */
    private Types mTypeUtils;

    /**
     * 文件生成器 类/资源
     */
    private Filer mFilerUtils;

    private LogUtils mLogUtils;

    private String mModelName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mLogUtils = LogUtils.getInstance(processingEnv.getMessager());
        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mFilerUtils = processingEnv.getFiler();

        // 参数是模块名 为了防止多模块/组件化开发的时候 生成相同的 xx$$ROOT$$文件
        Map<String, String> options = processingEnv.getOptions();
        if (!Utils.isEmpty(options)) {
            mModelName = options.get(Constant.ARGUMENTS_NAME);
        }

        if (Utils.isEmpty(mModelName)) {
            throw new RuntimeException("Not set processor moudleName option !");
        }
        mLogUtils.i("init RouterProcessor " + mModelName + " success !");
    }

    /**
     * @param set 使用了支持处理注解的节点集合
     * @param roundEnv 表示当前或是之前的运行环境,可以通过该对象查找找到的注解。
     * @return true 表示后续处理器不会再处理(已经处理)
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (!Utils.isEmpty(set)) {
            // 获取Route注解过的节点集合
            Set<? extends Element> rootElements = roundEnv.getElementsAnnotatedWith(Route.class);
            if (!Utils.isEmpty(rootElements)) {
                processorRouter(rootElements);
            }
            return true;
        }
        return false;
    }

    /**
     * 处理获取到的@Route注解的节点集合
     * @param rootElements
     */
    private void processorRouter(Set<? extends Element> rootElements) {
        // 获得Activity类的节点信息
        TypeElement activity = mElementUtils.getTypeElement(Constant.ACTIVITY);

        TypeElement service = mElementUtils.getTypeElement(Constant.ISERVICE);
        for (Element element : rootElements) {
            RouteMeta routeMeta;
            TypeMirror typeMirror = element.asType();
            mLogUtils.i("Route class: " + typeMirror.toString());
            Route route = element.getAnnotation(Route.class);
            if (mTypeUtils.isSubtype(typeMirror, activity.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ACTIVITY, route, element);
            } else if (mTypeUtils.isSubtype(typeMirror, service.asType())) {
                routeMeta = new RouteMeta(RouteMeta.Type.ISERVICE, route, element);
            } else {
                throw new RuntimeException("Just support Activity or IService Route: " + element);
            }
            categories(routeMeta);
        }

        TypeElement iRouteRoot = mElementUtils.getTypeElement(Constant.IROUTE_ROOT);
        TypeElement iRouteGroup = mElementUtils.getTypeElement(Constant.IROUTE_GROUP);

        // 生成Root类 作用：记录<分组，对应的Group类>
        generatedRoot(iRouteRoot, iRouteGroup);

        // 生成Group记录分组表
        generatedGroup(iRouteGroup);
    }

    /**
     * 生成Root类 作用：记录<分组，对应的Group类>
     * @param iRouteRoot
     * @param iRouteGroup
     */
    private void generatedRoot(TypeElement iRouteRoot, TypeElement iRouteGroup) {
        // 创建参数类型 Map<String,Class<? extends IRouteGroup>> routes>
        // Wildcard 通配符
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(iRouteGroup))));
        // 创建参数的实现 Map<String,Class<? extends IRouteGroup>> routes> routes
        ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "routes").build();
        // 函数(方法)的生成实现 public void loadInto(Map<String,Class<? extends IRouteGroup>> routes> routes)
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constant.METHOD_LOAD_INTO)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec);
        // 函数体
        for (Map.Entry<String, String> entry : mRootMap.entrySet()) {
            methodBuilder.addStatement("routes.put($S, $T.class)", entry.getKey(),
                    ClassName.get(Constant.PACKAGE_OF_GENERATE_FILE, entry.getValue()));
        }
        // 生成Root类
        String className = Constant.NAME_OF_ROOT + mModelName;
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .addSuperinterface(ClassName.get(iRouteRoot))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuilder.build())
                .build();
        try {
            JavaFile.builder(Constant.PACKAGE_OF_GENERATE_FILE, typeSpec)
                    .build()
                    .writeTo(mFilerUtils);
            mLogUtils.i("Generated RouteRoot：" + Constant.PACKAGE_OF_GENERATE_FILE + "." + className);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成Group记录分组表
     * @param iRouteGroup
     */
    private void generatedGroup(TypeElement iRouteGroup) {
        // 创建参数类型 Map<String, RouteMeta> atlas
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ClassName.get(RouteMeta.class));
        // 创建参数的实现 Map<String, RouteMeta> atlas
        ParameterSpec parameterSpec = ParameterSpec.builder(parameterizedTypeName, "atlas").build();
        // 函数体
        for (Map.Entry<String, List<RouteMeta>> entry : mGroupMap.entrySet()) {
            // 函数的生成实现 public void loadInto(Map<String, RouteMeta> atlas)
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constant.METHOD_LOAD_INTO)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(parameterSpec);
            String groupName = entry.getKey();
            List<RouteMeta> groupData = entry.getValue();
            for (RouteMeta routeMeta : groupData) {
                // 函数体的添加
                methodBuilder.addStatement("atlas.put($S,$T.build($T.$L,$T.class,$S,$S))",
                        routeMeta.getPath(),
                        ClassName.get(RouteMeta.class),
                        ClassName.get(RouteMeta.Type.class),
                        routeMeta.getType(),
                        ClassName.get(((TypeElement) routeMeta.getElement())),
                        routeMeta.getPath(),
                        routeMeta.getGroup());
            }
            String groupClassName = Constant.NAME_OF_GROUP + groupName;
            TypeSpec typeSpec = TypeSpec.classBuilder(groupClassName)
                    .addSuperinterface(ClassName.get(iRouteGroup))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())
                    .build();
            try {
                JavaFile.builder(Constant.PACKAGE_OF_GENERATE_FILE, typeSpec)
                        .build()
                        .writeTo(mFilerUtils);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRootMap.put(groupName, groupClassName);
        }
    }

    /**
     * 检查是否配置 group 如果没有配置 则从path截取出组名
     * @param routeMeta
     */
    private void categories(RouteMeta routeMeta) {
        if (routeVerify(routeMeta)) {
            mLogUtils.i("Group : " + routeMeta.getGroup() + " path = " + routeMeta.getPath());
            // 分组与组中的路由信息
            List<RouteMeta> routeMetas = mGroupMap.get(routeMeta.getGroup());
            if (Utils.isEmpty(routeMetas)) {
                routeMetas = new ArrayList<>();
                routeMetas.add(routeMeta);
                mGroupMap.put(routeMeta.getGroup(), routeMetas);
            } else {
                routeMetas.add(routeMeta);
            }
        } else {
            mLogUtils.i("Group info error:" + routeMeta.getPath());
        }
    }

    /**
     * 验证 path 路由地址的合法性
     * @param routeMeta
     * @return
     */
    private boolean routeVerify(RouteMeta routeMeta) {
        String path = routeMeta.getPath();
        String group = routeMeta.getGroup();

        // 必须以 / 开头来指定路由地址
        if (!path.startsWith("/")) {
            return false;
        }
        // 如果 group 没有设置 我们从 path 中获得group
        if (Utils.isEmpty(group)) {
            String defaultGroup = path.substring(1, path.indexOf("/", 1));
            // 截取出的 group 还是空
            if (Utils.isEmpty(defaultGroup)) {
                return false;
            }
            routeMeta.setGroup(defaultGroup);
        }
        return true;
    }
}