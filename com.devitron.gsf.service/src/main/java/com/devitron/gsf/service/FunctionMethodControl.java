package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

public class FunctionMethodControl {

    private HashMap<String, MethodNode> functionMap = new HashMap<String, MethodNode>();
    static private FunctionMethodControl fmc = new FunctionMethodControl();

    private FunctionMethodControl() { }

    static public FunctionMethodControl getFunctionMethodControl() { return fmc; }

    public void putMethod(String functionName, Method method) {

        MethodNode mn = null;

        if (functionMap.containsKey(functionName)) {
            mn = functionMap.get(functionName);
        } else {
            mn = new MethodNode();
            functionMap.put(functionName, mn);
        }

        mn.method = method;

    }

    public Method getMethod(String functionName) {

        Method m = null;

        MethodNode mn = functionMap.get(functionName);
        if (mn != null) {
            m = mn.method;
        }

        return m;
    }



    public class MethodNode {
        private Method method;
        private Class firstArgClass;
        private Class returnTypeClass;
    }

}
