package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

/**
 * Created by bleoo on 2017/9/30.
 */

public class BindingClass {

    private static final ClassName VIEW = ClassName.get("android.view", "View");

    private String packageName;
    private String className;

    private HashMap<Integer, VariableElement> varMap;

    public BindingClass(String packageName, String className) {
        this.className = className;
        this.packageName = packageName;
        varMap = new LinkedHashMap<>();
    }

    public void putVariableElement(int id, VariableElement element) {
        varMap.put(id, element);
    }

    public void brewJava(Filer filer) {

        ClassName targetActivityType = ClassName.get(packageName, className);

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetActivityType, "activity");

        for (Map.Entry<Integer, VariableElement> entry : varMap.entrySet()) {
            int id = entry.getKey();

            // 参数名
            String name = entry.getValue().getSimpleName().toString();
            // 参数对象类
            String type = entry.getValue().asType().toString();

            constructor.addStatement("activity." + name + " = (" + type + ")activity.findViewById(" + id + ")", VIEW);
        }

        TypeSpec typeSpec = TypeSpec.classBuilder(className + "_ViewBinding")//HelloWorld是类名
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addMethod(constructor.build())  //在类中添加方法
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
