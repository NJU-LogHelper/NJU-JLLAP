package edu.nju.util;

import com.intellij.codeInspection.LocalQuickFix;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by chentiange on 2018/5/22.
 */
public class LevelSequenceUtil{

    private static final String pythonFilePath = LevelSequenceUtil.class.getResource("/edu/nju/util/python/itefile.py").getFile();
    //win10 mac路径问题
    private static final String projectsPath = LevelSequenceUtil.class.getResource("/projects").getPath();
        /**
         * python
         * @param type
         * @return
         */
    private static List<String> getLevelSequence(String type){
        Properties properties = new Properties();
        properties.put("python.console.encoding", "UTF-8");
        properties.put("python.security.respectJavaAccessibility", "false");
        properties.put("python.import.os", "false");
        properties.put("python.import.collections", "false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops,properties,new String[0]);

        PythonInterpreter interpreter =  new PythonInterpreter();
        interpreter.execfile(pythonFilePath);

        PyFunction function = interpreter.get("getSeq",PyFunction.class);
        PyObject pyObject = function.__call__(new PyString(type),new PyString(projectsPath));
        String liststr = pyObject.toString();
        String[] levelparts = liststr.split(",");
        List<String> res = new ArrayList<>();
        for (int i= 0;i<levelparts.length;++i){
            String levelpart = levelparts[i];
            String temp = levelpart;
            int firstindex = temp.indexOf("\'");
            temp = temp.substring(firstindex+1);
            int secondindex = temp.indexOf("\'");
            temp = temp.substring(0,secondindex);
            res.add(temp);
        }
        return res;
    }

    public static List<LocalQuickFix> getQuickfixSequence(String className, String type, String ope){
        Class<?> classc = null;
        try {
            classc = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object obj = null;
        List<String> levelseq = getLevelSequence(type);
        List<LocalQuickFix> quickFixes = new ArrayList<>();
        try {
            obj = classc.newInstance();
            System.out.println(levelseq.size());
            Field[] fields = classc.getDeclaredFields();
            // 这里设置访问权限为true
            for (int i=0; i<levelseq.size();++i){
                String level = levelseq.get(i);
                System.out.println(level);

                for (int j=0;j<fields.length;++j){
                    Field field = fields[j];
                    field.setAccessible(true);
                    System.out.println(field.getName());
                    if (field.getType().toString().contains("LocalQuickFix") && field.getName().toLowerCase().contains(level)&&field.getName().toLowerCase().contains(ope)){
                        quickFixes.add((LocalQuickFix)field.get(obj));
                        System.out.println("add: "+field.getName());

                    }
                }
            }
            for (int i = 0;i<fields.length;++i){
                if(fields[i].getType().toString().contains("LocalQuickFix") && !quickFixes.contains((LocalQuickFix)fields[i].get(obj))){
                    quickFixes.add((LocalQuickFix)fields[i].get(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i =0;i<quickFixes.size();++i){
            System.out.println(quickFixes.get(i).getName());
        }
        return quickFixes;
    }

    public static String getPercentStr(String type){
        Properties properties = new Properties();
        properties.put("python.console.encoding", "UTF-8");
        properties.put("python.security.respectJavaAccessibility", "false");
        properties.put("python.import.os", "false");
        properties.put("python.import.collections", "false");
        Properties preprops = System.getProperties();
        PythonInterpreter.initialize(preprops,properties,new String[0]);
        PythonInterpreter interpreter =  new PythonInterpreter();
        interpreter.execfile(pythonFilePath);
        PyFunction function = interpreter.get("getPercent",PyFunction.class);
        PyObject pyObject = function.__call__(new PyString(type),new PyString(projectsPath));
        String liststr = pyObject.toString();
        String res = "recommand "+liststr;
        return res;

    }

    public static void main(String[] args) {
        System.out.println(getPercentStr("if"));

    }

}
