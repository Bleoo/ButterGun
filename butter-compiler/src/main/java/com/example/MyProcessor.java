package com.example;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    private Types mTypeUtils;
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        HashMap<String, BindingClass> bindingClassMap = new LinkedHashMap<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {

            /*检查类型*/
            if (!(element instanceof VariableElement)) {
                return false;
            }
            VariableElement variableElement = (VariableElement) element;

            /*获取类信息*/
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            /*类的绝对路径*/
            String qualifiedName = typeElement.getQualifiedName().toString();
            /*类名*/
            String clsName = typeElement.getSimpleName().toString();
            /*获取包名*/
            String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();

            BindView annotation = variableElement.getAnnotation(BindView.class);
            int id = annotation.value();

            /*参数名*/
            String name = variableElement.getSimpleName().toString();
            /*参数对象类*/
            String type = variableElement.asType().toString();

            BindingClass bindingClass = bindingClassMap.get(qualifiedName);
            if (bindingClass == null) {
                bindingClass = new BindingClass(packageName, clsName);
                bindingClassMap.put(qualifiedName, bindingClass);
            }
            bindingClass.putVariableElement(id, variableElement);
        }

        for (Map.Entry<String, BindingClass> entry : bindingClassMap.entrySet()) {
            entry.getValue().brewJava(mFiler);
        }
        return true;
    }

    private void note(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.NOTE,
                String.format(msg, args),
                e);
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}
