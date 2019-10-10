package com.leon.experimental;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class JsoupExample {

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse("<html/>");
        document.attr("lang", "en");

        Element head = document.head();
        head.append("<style> .green {color: green} .red {color: red} .inline {display: inline} </style>");

        Element body = document.body();
        body.appendChild(document.createElement("br"));
        body.appendChild(document.createElement("br"));

        Element validationResultLabel = document.createElement("p").addClass("inline").appendChild(document.createElement("b").html("Validation result: "));
        body.appendChild(validationResultLabel);

        Element validationResult = document.createElement("p").addClass("inline");
        boolean valid = new Random().nextBoolean();
        if (valid) {
            validationResult.addClass("green").html("PASSED");
        } else {
            validationResult.addClass("red").html("FAILED");
        }
        body.appendChild(validationResult);

        if (!valid) {
            body.appendChild(document.createElement("br"));
            body.appendChild(document.createElement("br"));

            Element validationReasonLabel = document.createElement("p").addClass("inline").appendChild(document.createElement("b").html("Validation reason: "));
            body.appendChild(validationReasonLabel);
            body.appendChild(document.createElement("p").addClass("inline").html("The stop count reduces 10%"));
        }

        FileOutputStream outputStream = new FileOutputStream(new File("C:/tmp/test.html"));
        outputStream.write(document.outerHtml().getBytes());
        outputStream.close();
    }

}
