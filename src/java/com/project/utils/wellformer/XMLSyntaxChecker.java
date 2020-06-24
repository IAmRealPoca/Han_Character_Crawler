/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.utils.wellformer;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Poca
 */
//import static com.xmlchecker.SyntaxState.*;
public class XMLSyntaxChecker {

    private Character quote;

    private String convert(Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String value = entry.getValue()
                    .replace("&", "&amp;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&apos;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;");

            builder.append(entry.getKey())
                    .append("=")
                    .append("\"")
                    .append(value)
                    .append("\"")
                    .append(" ");
        }
        String result = builder.toString().trim();
        if (!result.equals("")) {
            result = " " + result;
        }
        return result;
    }

    public String check(String src) {
        src = src + " ";
        char[] reader = src.toCharArray();
        StringBuilder writer = new StringBuilder();

        StringBuilder openTag = new StringBuilder();
        boolean isEmptyTag = false;
        boolean isOpenTag = false;
        boolean isCloseTag = false;
        StringBuilder closeTag = new StringBuilder();
        StringBuilder attrName = new StringBuilder();
        StringBuilder attrValue = new StringBuilder();
        Map<String, String> attributes = new HashMap<>();

        StringBuilder content = new StringBuilder();

        Stack<String> stack = new Stack<>();

        String state = SyntaxState.CONTENT;

        for (int i = 0; i < reader.length; i++) {
            char c = reader[i];

            switch (state) {
                case SyntaxState.CONTENT:
                    if (c == SyntaxState.LT) {
                        state = SyntaxState.OPEN_BRACKET;
                        writer.append(content.toString()
                                .trim()
                                .replace("&", "&amp;"));

                    } else {
                        content.append(c);
                    }
                    break;
                case SyntaxState.OPEN_BRACKET:
                    if (SyntaxState.isStartTagChars(c)) {
                        state = SyntaxState.OPEN_TAG_NAME;
                        isOpenTag = true;
                        isCloseTag = false;
                        isEmptyTag = false;

                        openTag.setLength(0);
                        openTag.append(c);
                    } else if (c == SyntaxState.SLASH) {
                        state = SyntaxState.CLOSE_TAG_SLASH;

                        isOpenTag = false;
                        isCloseTag = true;
                        isEmptyTag = false;
                    }
                    break;
                case SyntaxState.OPEN_TAG_NAME:
                    if (SyntaxState.isTagChars(c)) {
                        openTag.append(c);
                    } else if (SyntaxState.isSpace(c)) {
                        state = SyntaxState.TAG_INNER;
                        attributes.clear(); //ready to write new tag's attributes
                    } else if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                    } else if (c == SyntaxState.SLASH) {
                        state = SyntaxState.EMPTY_SLASH;
                    }
                    break;
                case SyntaxState.TAG_INNER:
                    if (SyntaxState.isSpace(c)) {
                        // skip spaces
                    } else if (SyntaxState.isStartAttrChars(c)) {
                        state = SyntaxState.ATTR_NAME;

                        attrName.setLength(0);
                        attrName.append(c);
                    } else if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                    } else if (c == SyntaxState.SLASH) {
                        state = SyntaxState.EMPTY_SLASH;
                    }
                    break;
                case SyntaxState.ATTR_NAME:
                    if (SyntaxState.isAttrChars(c)) {
                        attrName.append(c);
                    } else if (c == SyntaxState.EQ) {
                        state = SyntaxState.EQUAL;
                    } else if (SyntaxState.isSpace(c)) {
                        state = SyntaxState.EQUAL_WAIT;
                    } else {
                        if (c == SyntaxState.SLASH) {
                            attributes.put(attrName.toString(), "true");
                            state = SyntaxState.EMPTY_SLASH;
                        } else if (c == SyntaxState.GT) {
                            attributes.put(attrName.toString(), "true");
                            state = SyntaxState.CLOSE_BRACKET;
                        }
                    }
                    break;
                case SyntaxState.EQUAL_WAIT:
                    if (SyntaxState.isSpace(c)) {

                    } else if (c == SyntaxState.EQ) {
                        state = SyntaxState.EQUAL;
                    } else {
                        if (SyntaxState.isStartAttrChars(c)) {
                            attributes.put(attrName.toString(), "true");
                            state = SyntaxState.ATTR_NAME;
                            attrName.setLength(0);
                            attrName.append(c);
                        }
                    }
                    break;
                case SyntaxState.EQUAL:
                    if (SyntaxState.isSpace(c)) {

                    } else if (c == SyntaxState.D_QUOT || c == SyntaxState.S_QUOT) {
                        quote = c;
                        state = SyntaxState.ATTR_VALUE_Q;

                        attrValue.setLength(0);
                    } else if (!SyntaxState.isSpace(c) && c != SyntaxState.GT) {
                        state = SyntaxState.ATTR_VALUE_NQ;

                        attrValue.setLength(0);
                        attrValue.append(c);
                    }
                    break;
                case SyntaxState.ATTR_VALUE_Q:
                    if (c != quote) {
                        attrValue.append(c);
                    } else if (c == quote) {
                        state = SyntaxState.TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case SyntaxState.ATTR_VALUE_NQ:
                    if (!SyntaxState.isSpace(c) && c != SyntaxState.GT) {
                        attrValue.append(c);
                    } else if (SyntaxState.isSpace(c)) {
                        state = SyntaxState.TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    } else if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case SyntaxState.EMPTY_SLASH:
                    if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                        isEmptyTag = true;
                    }
                    break;
                case SyntaxState.CLOSE_TAG_SLASH:
                    if (SyntaxState.isStartTagChars(c)) {
                        state = SyntaxState.CLOSE_TAG_NAME;
                        closeTag.setLength(0);
                        closeTag.append(c);
                    }
                    break;
                case SyntaxState.CLOSE_TAG_NAME:
                    if (SyntaxState.isTagChars(c)) {
                        closeTag.append(c);
                    } else if (SyntaxState.isSpace(c)) {
                        state = SyntaxState.WAIT_END_TAG_CLOSE;
                    } else if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                    }
                    break;
                case SyntaxState.WAIT_END_TAG_CLOSE:
                    if (SyntaxState.isSpace(c)) {

                    } else if (c == SyntaxState.GT) {
                        state = SyntaxState.CLOSE_BRACKET;
                    }
                    break;
                case SyntaxState.CLOSE_BRACKET:
                    if (isOpenTag) {
                        String openTagName = openTag.toString().toLowerCase();

                        if (SyntaxState.INLINE_TAGS.contains(openTagName)) {
                            isEmptyTag = true;
                        }

                        writer.append(SyntaxState.LT)
                                .append(openTagName)
                                .append(convert(attributes))
                                .append(isEmptyTag ? "/" : "")
                                .append(SyntaxState.GT);

                        attributes.clear();
                        //Stack
                        if (!isEmptyTag) {
                            stack.push(openTagName);
                        }
                    } else if (isCloseTag) {
                        String closeTagName = closeTag.toString().toLowerCase();
                        if (!stack.isEmpty() && stack.contains(closeTagName)) {
                            while (!stack.isEmpty() && !stack.peek().equals(closeTagName)) {
                                writer.append(SyntaxState.LT)
                                        .append(SyntaxState.SLASH)
                                        .append(stack.pop())
                                        .append(SyntaxState.GT);
                            }
                            if (!stack.isEmpty() && stack.peek().equals(closeTagName)) {
                                writer.append(SyntaxState.LT)
                                        .append(SyntaxState.SLASH)
                                        .append(stack.pop())
                                        .append(SyntaxState.GT);
                            }
                        }
                    }

                    if (c == SyntaxState.LT) {
                        state = SyntaxState.OPEN_BRACKET;
                    } else {
                        state = SyntaxState.CONTENT;
                        content.setLength(0);
                        content.append(c);
                    }
                    break;

            } // end switch(state)

        } // end for (i < reader.length)
        if (SyntaxState.CONTENT.equals(state)) {
            writer.append(content.toString().trim().replace("&", "&amp;"));
        }
        while (!stack.isEmpty()) {
            writer.append(SyntaxState.LT)
                    .append(SyntaxState.SLASH)
                    .append(stack.pop())
                    .append(SyntaxState.GT);
        }
        return writer.toString();
    }
}
