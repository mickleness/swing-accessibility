package com.pump.ax.mac;

import com.pump.ax.AXHelper;
import com.pump.ax.Feature;

import javax.accessibility.Accessible;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.function.Supplier;

/**
 * This strips out HTML from accessibility names.
 *
 * This is a work-around for <a href="https://bugs.openjdk.org/browse/JDK-4949105">JDK-4949105</a>.
 */
public class FixReadingHTML extends Feature {
    final CAccessibilityHandler handler = new CAccessibilityHandler() {

        @Override
        protected String getAccessibleName(Accessible a, Component c) {

            // TODO: compare this fix vs the fix for
            // https://bugs.openjdk.org/browse/JDK-4949105

            // TODO: can we set up a similar architecture so we can apply this solution on Windows?

            String axName = super.getAccessibleName(a, c);
            if (axName != null && axName.startsWith("<html>"))
                axName = stripHtml(axName);
            return axName;
        }

        public String stripHtml(String html) {
            if (html == null)
                return null;
            StringBuilder sb = new StringBuilder();
            HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
                public void handleText(char[] data, int pos) {
                    sb.append(data);
                }
            };

            try {
                new ParserDelegator().parse(new StringReader(html), callback, true);
            } catch (IOException e) {
                // we don't expect IO issues since our `html` String is in memory
                throw new RuntimeException(e);
            }
            return sb.toString();
        }
    };

    @Override
    public void install() throws UnsupportedOperationException {
        if (!isSupported())
            throw new UnsupportedOperationException(CAccessibilityController.UNSUPPORTED_EXCEPTION_MESSAGE);
        CAccessibilityController.get().addHandler(handler);
    }

    @Override
    public void uninstall() {
        CAccessibilityController.get().removeHandler(handler);
    }

    @Override
    public boolean isRecommended() {
        return AXHelper.isMac && AXHelper.javaVersion < 13;
    }

    @Override
    public boolean isSupported() {
        return CAccessibilityController.get().isValid();
    }
}
