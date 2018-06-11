package edu.nju.util.loggingutil;

import com.intellij.psi.PsiStatement;
import edu.nju.util.loggingutil.logginglevel.JavaLoggingLevel;
import edu.nju.util.loggingutil.logginglevel.Log4jLoggingLevel;
import edu.nju.util.loggingutil.logginglevel.Slf4JLoggingLevel;
import com.intellij.psi.PsiClass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chentiange on 2018/5/6.
 */
public class LoggingUtil {
    public static boolean isLogged(PsiStatement psiStatement){
        if (psiStatement.getNextSibling().getNextSibling().getText().startsWith("log.")){
            return true;
        }else {
            return false;
        }
    }


    public static String getCurrentClassWholeName(PsiClass psiClass){
        return psiClass.getQualifiedName();
    }

    public static String getLevelStringById(Object level, int typeId){
        String levelString = "";
        if (typeId == 0){
            levelString = ((JavaLoggingLevel) level).getLevelString((JavaLoggingLevel) level);
        }else if (typeId == 1){
            levelString = ((Log4jLoggingLevel) level).getLevelString((Log4jLoggingLevel) level);
        }else if (typeId == 2){
            levelString = ((Slf4JLoggingLevel) level).getLevelString((Slf4JLoggingLevel) level);
        }
        return levelString;
    }
}
