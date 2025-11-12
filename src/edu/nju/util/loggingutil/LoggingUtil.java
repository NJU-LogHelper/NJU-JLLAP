package edu.nju.util.loggingutil;

import com.intellij.psi.PsiStatement;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import edu.nju.util.loggingutil.logginglevel.Log4jLoggingLevel;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import com.intellij.psi.PsiClass;

/**
 * Created by chentiange on 2018/5/6.
 * 日志工具：判断语句是否已打日志，获取类全名，根据日志框架类型输出对应级别字符串
 */
public class LoggingUtil {

    /**
     * 简单判断下一条语句是否已存在 log. 开头，避免重复插入
     */
    public static boolean isLogged(PsiStatement psiStatement) {
        return psiStatement.getNextSibling().getNextSibling().getText().startsWith("log.");
    }

    /**
     * 获取类的完全限定名
     */
    public static String getCurrentClassWholeName(PsiClass psiClass) {
        return psiClass.getQualifiedName();
    }

    /**
     * 根据日志框架类型（0-JavaUtil,1-Log4j,2-Slf4j）返回对应的级别字符串
     */
    public static String getLevelStringById(Object level, int typeId) {
        switch (typeId) {
            case 0:
                return ((JavaLoggingLevel) level).getLevelString((JavaLoggingLevel) level);
            case 1:
                return ((Log4jLoggingLevel) level).getLevelString((Log4jLoggingLevel) level);
            case 2:
                return ((Slf4JLoggingLevel) level).getLevelString((Slf4JLoggingLevel) level);
            default:
                return "";
        }
    }
}