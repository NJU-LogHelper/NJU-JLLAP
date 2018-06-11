package edu.nju.util;

import edu.nju.actions.CriticalInspectionConfigAction;
import edu.nju.config.Inspection.defaults.LogContext;
import edu.nju.config.Inspection.defaults.LogContextList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;

public class XmlUtil {
    private  ArrayList<LogContext> logContextArrayList = new ArrayList<>();
//    private static final String XMLPATH="/Users/chentiange/Downloads/LogHelper/resources/LogContext.xml";
    private static final String XMLPATH=CriticalInspectionConfigAction.class.getResource("/LogContext.xml").getFile();

//    final String fileNameForMethod =
    /**
     * xml文件配置转换为对象
     * @param xmlPath  xml文件路径
     * @param load    java对象.Class
     * @return    java对象
     * @throws JAXBException 异常
     */
    public static Object xmlToBean(String xmlPath,Class<?> load) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new File(xmlPath));
    }

    /**
     * java对象转换为xml文件
     * @param load    java对象.Class
     * @return    xml文件的String
     * @throws JAXBException
     */
    public static String beanToXml(Object obj,Class<?> load) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj,writer);
        return writer.toString();
    }
    public XmlUtil(){
        try {
            setLogContextArrayList((ArrayList<LogContext>) ((LogContextList)xmlToBean(XMLPATH,LogContextList.class)).getLogContexts());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LogContext> getLogContextArrayList() {
        return logContextArrayList;
    }

    public void setLogContextArrayList(ArrayList<LogContext> logContextArrayList) {
        this.logContextArrayList = logContextArrayList;
    }

    public static void main(String[] args) throws JAXBException, IOException {
//        List<String> keywords1= Arrays.asList("hhh", "hehehe");
//        List<String> keywords2=Arrays.asList("666","233");
//        LogContext logContext1=new LogContext("CatchClause","description1","quickfix1","template1","error",keywords1);
//        LogContext logContext2=new LogContext("CatchClause2","description2","quickfix2","template2","info",keywords2);
//        LogContext logContext3=new LogContext("CatchClause3","description1","quickfix1","template1","error",null);
//        List<LogContext> logContextList=new ArrayList<>();
//        logContextList.add(logContext1);
//        logContextList.add(logContext2);
//        logContextList.add(logContext3);
//        LogContextList logContextList1=new LogContextList(logContextList);
//        String str=beanToXml(logContextList1,LogContextList.class);
//        BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(XMLPATH)));
//        bfw.write(str);
//        bfw.close();

//        XmlUtil xu=new XmlUtil();
//        System.out.println(xu.getLogContextArrayList().toString());

//        CriticalInspectionConfigAction.class.getResource("/LogContext.xml");


        String path = CriticalInspectionConfigAction.class.getResource("/LogContext.xml").getFile();
        File file = new File(path);
//        System.out.println(file.getName());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str = bufferedReader.readLine();
        while (str!=null){
            System.out.println(str);
            str = bufferedReader.readLine();
        }




    }
}
