package edu.nju.codeInspection;

import com.intellij.codeInspection.InspectionToolProvider;

/**
 * @author max
 */
public class InspectionProvider implements InspectionToolProvider {
    public Class[] getInspectionClasses() {
        return new Class[]{
                AssertInspection.class,
                BranchStatementsInspection.class,
                ComparingReferencesInspection.class,
                CriticalOpeInspection.class,
                ExceptionInspection.class,
                JDBCInspection.class,
                SwitchInspection.class,
                ThreadInspection.class,
                TotalInspection.class
                };
    }
}
