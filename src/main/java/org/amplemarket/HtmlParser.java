package org.amplemarket;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    private final ObjectMapper objectMapper;

    public HtmlParser() {
        objectMapper = new ObjectMapper();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void parseHtml() throws IOException {
        Document doc = Jsoup.connect("https://news.ycombinator.com/").get();

        List<Post> postItems = new ArrayList<>();

        doc.body().getElementsByClass("athing").forEach(element -> {
            String id = element.id();

            Element titleElement = element.getElementsByClass("titleline").get(0).firstElementChild();

            Element rankElement = element.getElementsByClass("rank").get(0);
            Element siblingRowElement = element.nextElementSibling();

            Element scoreElement = siblingRowElement != null ? siblingRowElement.getElementById("score_" + id) : null;

            Long score = null;
            if (scoreElement != null) {
                score = Long.valueOf(getHtmlFromElement(scoreElement).replaceAll(" points", ""));
            }

            Post postItem = new Post(Long.valueOf(getHtmlFromElement(rankElement).replaceAll("\\.", "")), getHtmlFromElement(titleElement), score, getAttrFromElement(titleElement, "href"));

            postItems.add(postItem);
        });

        System.out.println(objectMapper.writeValueAsString(postItems));
    }

    String getHtmlFromElement(Element element) {
        if (element == null) {
            return null;
        }

        return element.html();
    }

    String getAttrFromElement(Element element, String attrName) {
        if (element == null) {
            return null;
        }

        return element.attr(attrName);
    }

    public static class Post {

        private Long position;
        private String title;
        private Long points;
        private String link;

        public Post(Long position, String title, Long points, String link) {
            this.position = position;
            this.title = title;
            this.points = points;
            this.link = link;
        }

        public Long getPosition() {
            return position;
        }

        public void setPosition(Long position) {
            this.position = position;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Long getPoints() {
            return points;
        }

        public void setPoints(Long points) {
            this.points = points;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
