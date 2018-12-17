package com.bkjk.infra.router_compiler.annotation;

import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Author: zhouzhenhua
 * Date: 2018/12/17
 * Version: 1.0.0
 * Description:测试版注解处理器
 */
@AutoService(Processor.class)
public class HenryProcessor extends AbstractProcessor {

    private static final String SUFFIX = "$$HENRY";

    private Types mTypeUtils;
    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 初始化需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 处理注解解析，并生成Java文件
        if (set == null || set.isEmpty()) {
            info(">>> set is null... <<<");
            return true;
        }
        info(">>> Found field, start... <<<");

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(HenryAnnotation.class);

        if (elements == null || elements.isEmpty()) {
            info(">>> elements is null... <<<");
            return true;
        }

        // 遍历所有被注解了@Factory的元素
        for (Element annotatedElement : elements) {
            // 检查被注解为@Factory的元素是否是一个类
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s",
                        HenryAnnotation.class.getSimpleName());
                return true; // 退出处理
            }
            analysisAnnotated(annotatedElement);
        }
        return true;
    }

    /**
     * 解析并生成代码
     *
     * @param annotatedElement
     */
    private void analysisAnnotated(Element annotatedElement) {
        HenryAnnotation henryAnnotation = annotatedElement.getAnnotation(HenryAnnotation.class);
        String name = henryAnnotation.name();
        String text = henryAnnotation.text();
        String newClassName = name + SUFFIX;
        StringBuilder builder = new StringBuilder()
                .append("package com.bkjk.infra.router_compiler.annotation.demoprocessor.auto;\n\n")
                .append("public class ")
                .append(newClassName)
                .append(" {\n\n")
                .append("\tpublic String getMessage() {\n")
                .append("\t\treturn \"");
        builder.append(text).append(" ").append(name).append(" !\\n");
        builder.append("\";\n")  // end return
                .append("\t}\n") // close method
                .append("}\n");  // close class
        try {
            JavaFileObject source = mFiler.createSourceFile("com.bkjk.infra.router_compiler.annotation.demoprocessor.auto." + newClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        info(">>> analysisAnnotated is finish... <<<");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // 支持的Java版本
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // 支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(HenryAnnotation.class.getCanonicalName());
        return annotations;
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args));
    }
}
