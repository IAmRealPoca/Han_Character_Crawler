/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utils.wellformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Poca
 */
public class HTMLWellFormerUtils {

    public static String refineHTML(String src) {
        src = getBody(src);
        src = removeMiscellaneousTags(src);

        XMLSyntaxChecker xmlSyntaxChecker = new XMLSyntaxChecker();
        src = xmlSyntaxChecker.check(src);

        src = getBody(src);
        src = src.replaceAll("<br/>", "&lt;br/&gt;");
        return src;
    }

    private static String getBody(String src) {
        String result = src;
//        String expression = "<body.*?</body>";
        String expression = "<body[\\s\\S]+</body>";
        Pattern pattern = Pattern.compile(expression);

        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    private static String removeMiscellaneousTags(String src) {
        String result = src;

//        String expression = "<script.*?</script>";
        String expression = "<script[^>]*>([\\s\\S]*?)</script>";
        result = result.replaceAll(expression, "");

        expression = "<style[^>]*>([\\s\\S]*?)</style>";
        result = result.replaceAll(expression, "");
        
        expression = "<!--.*?-->";
        result = result.replaceAll(expression, "");

        expression = "&nbsp;?";
        result = result.replaceAll(expression, "");

        return result;
    }
}
