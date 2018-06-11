package edu.nju.quickfixes;

import com.intellij.codeInspection.LocalQuickFix;
import edu.nju.config.LogLevel;

public interface QuickFixFactory {
    LocalQuickFix createCatchClauseQuickFix(String logLevel, String logMessage, String quickFixName);
    LocalQuickFix createIfStmtQuickFix(String logLevel, String logMessage, String quickFixName);
    LocalQuickFix createMethodDeclarationQuickFix(String logLevel, String logMessage, String quickFixName);
    LocalQuickFix createTryStmtQuickFix(String logLevel, String logMessage, String quickFixName);
    LocalQuickFix createForeachStmtQuickFix(String logLevel, String logMessage, String quickFixName);
}
