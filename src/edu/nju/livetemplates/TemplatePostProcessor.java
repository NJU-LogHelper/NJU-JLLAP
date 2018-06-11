package edu.nju.livetemplates;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateOptionalProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;

public class TemplatePostProcessor implements TemplateOptionalProcessor {

	private static final Logger LOG = Logger.getInstance("#edu.nju.livetemplates.TemplatePostProcessor");

	private static final Key<Runnable> PENDING_RUNNABLE = Key.create("LOG_HELPER_PENDING_RUNNABLE");

	public static void schedule(PsiFile file, final Runnable runnable, boolean replace) {
		final Runnable existing = file.getUserData(PENDING_RUNNABLE);
		if (!replace && existing != null) {
			file.putUserData(PENDING_RUNNABLE, () -> {
                existing.run();
                runnable.run();
            });
		} else
			file.putUserData(PENDING_RUNNABLE, runnable);
	}


	/**
	 * {@inheritDoc}
	 */
	public void processText(Project project, Template template,
							Document document, RangeMarker rangeMarker, Editor editor) {
		PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(document);
		if (file != null) {
			// Process pending runnables
			Runnable runnable = file.getUserData(PENDING_RUNNABLE);
			if (runnable != null) {
				try {
					runnable.run();
				} catch (ProcessCanceledException e) {
					throw e;
				} catch (Throwable t) {
                    LOG.error(t);
				} finally {
					file.putUserData(PENDING_RUNNABLE, null);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Nls
	public String getOptionName() {
		return "Post-process log template";
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEnabled(Template template) {
		String id;
		try {
			id = String.valueOf(template.getId());
		} catch (IncompatibleClassChangeError e) {
			try {
				Class<?> cls = template.getClass();
				id = String.valueOf(cls.getMethod("getId").invoke(template));
			} catch (Exception ee) {
                LOG.error("Failed to retrieve the template id of '" + template +
						"', disabling template post-processor here.", e);
				return false;
			}
		}

		return id.startsWith("LogHelper-");
	}

	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(Template template, boolean b) {
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible(Template template) {
		return false;
	}
}
