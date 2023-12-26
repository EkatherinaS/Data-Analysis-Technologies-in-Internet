package com.rentappartment.server.model.Parsing;

import com.rentappartment.server.model.Address.Address;
import com.rentappartment.server.model.Address.AddressDao;
import com.rentappartment.server.model.Contact.Contact;
import com.rentappartment.server.model.Contact.ContactDao;
import com.rentappartment.server.model.Image.Image;
import com.rentappartment.server.model.Image.ImageDao;
import com.rentappartment.server.model.Offer.Offer;
import com.rentappartment.server.model.Offer.OfferDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rentappartment.server.ServerApplication.*;

@Service
public class EtagiParser extends parserClass {

    private static final ArrayList<String> xpathButtonsFirstParse = new ArrayList<>(){
        {
            add("/html/body/div[7]/div/div[2]/div[2]/ul/li[2]/button"); //russian language
            add("/html/body/div[7]/div/div[3]/a"); //accept language choice
            add("/html/body/div[1]/main/div/div[2]/section[1]/div[3]/div/div[2]/p/span[2]/button"); //show full description
            add("/html/body/div[1]/main/div/div[2]/section[2]/div/div/div[1]/div[3]/div/button");//show phone number
            add("/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/button"); //show more attributes
        }
    };
    private static final ArrayList<String> xpathButtons = new ArrayList<>(){
        {
            add("/html/body/div[1]/main/div/div[2]/section[1]/div[3]/div/div[2]/p/span[2]/button"); //show full description
            add("/html/body/div[1]/main/div/div[2]/section[2]/div/div/div[1]/div[3]/div/button");//show phone number
            add("/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/button"); //show more attributes
        }
    };
    private static boolean firstParse = true;

    private static final String fullDescriptionXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[3]/div/div[2]/p/span";
    private static final String contactPhoneNumberXpath = "/html/body/div[1]/main/div/div[2]/section[2]/div/div/div[1]/div[3]/div/a/span";
    private static final String fioXpath = "/html/body/div[1]/main/div/div[2]/section[2]/div/div/div[1]/div[2]/div/a";
    private static final String typeXpath = "/html/body/div[1]/main/div/div[4]/div/ol/li[3]/a/span";
    private static final String priceXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[2]/div/div/div[1]/div[1]/div/span";
    private static final String roomNumberXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/div[3]/ul/li[2]/span[2]";
    private static final String areaXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/div[3]/ul/li[3]/span[2]";
    private static final String kitchenAreaXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/div[3]/ul/li[4]/span[2]";
    private static final String yearXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/div[4]/ul/li[1]/span[2]";
    private static final String floorNumbersXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[4]/div/div[4]/ul/li[2]/span[2]";

    private static final String imagesButtonXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[1]/div/div/div/div/div/div[2]/button";
    private static final String imagePatternXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[1]/div/div[2]/div/div/div[1]/div[2]/div[1]/div/div[%s]/div/div/div/div[1]/div/div/img";
    private static final String prevImageButtonXpath = "/html/body/div[1]/main/div/div[2]/section[1]/div[1]/div/div[2]/div/div/div[1]/div[2]/button[1]";
    private static final String addressClass = "div.Vs6MQ";
    private static final String pagesNumberClass = "x0SgG Q7VHf";

    private static final String regexSplitPhoneNumber = "\\D+";
    private static final String regexAddress = "р-н (.+), ул\\. (.+), ([^ ]+).*";
    private static final String formatPhoneNumber = "+%s %s%s%s %s%s%s %s%s %s%s";

    private static final Logger etagiLogger = LoggerFactory.getLogger(EtagiParser.class);


    public void parse() {

        String url = "https://perm.etagi.com/realty_rent/";
        System.setProperty("http.proxyHost", "<<proxy host>>");

        String flatUrl;
        String pageUrl;
        Elements flatUrlList;

        //Get number of pages on website
        etagiLogger.info("Fetching " + url + "...");
        doc = documentFromUrl(url);
        int pageNumber = getNumberOfPages();

        //Link to the previous parsed flat
        String lastFlatLink = "";

        //Go through all pages
        for (int i = 1; i <= pageNumber; i++) {
            pageUrl = url + "?page=" + i;

            etagiLogger.info("Fetching " + pageUrl + "...");
            doc = documentFromUrl(pageUrl);
            if (doc != null) {
                flatUrlList = doc.select("a[href*=/realty_rent/]");

                //Go through all flats on the page
                for (Element flatUrlElement : flatUrlList) {
                    flatUrl = flatUrlElement.attr("abs:href");
                    if (flatUrl.length() > url.length()) {

                        //If it is a flat url and id does not match the previous one,
                        //parse flat url
                        if (Character.isDigit(flatUrl.charAt(url.length())) &&
                                !Objects.equals(lastFlatLink, flatUrl)) {
                            lastFlatLink = flatUrl;
                            parseFlat(flatUrl);
                            firstParse = false;
                        }
                    }
                }
            }
        }
    }


    //Parse a flat page by url
    private void parseFlat(String url) {

        Double area;
        Integer floor = null;
        String fullDescription;
        Double kitchenArea;
        Double price;
        Integer roomNumber;
        String type;
        String addressText;
        Integer year;
        Integer floorsNumber = null;
        String contactPhoneNumber;
        String fio;
        ArrayList<Integer> floorNumbers;
        ArrayList<Integer> imageNumber;
        String imageXpath;
        String imageUrl;
        String district = null;
        String street = null;
        String house = null;

        etagiLogger.info("Fetching " + url + "...");
        if (firstParse) {
            doc = documentFromUrl(url, xpathButtonsFirstParse);
        } else {
            doc = documentFromUrl(url, xpathButtons);
        }

        //Get data from website
        fullDescription = getStringValue(getTextFromXpath(fullDescriptionXpath));
        Pattern pattern = Pattern.compile(regexSplitPhoneNumber);
        Matcher matcher = pattern.matcher(getStringValue(getTextFromXpath(contactPhoneNumberXpath)));
        String[] values = matcher.replaceAll("").split("");
        contactPhoneNumber = String.format(formatPhoneNumber, values);

        fio = getStringValue(getTextFromXpath(fioXpath));
        type = switch (getStringValue(getTextFromXpath(typeXpath))) {
            case "Комнаты" -> "комната";
            default -> "квартира";
        };
        price = getDoubleValue(getTextFromXpath(priceXpath));
        addressText = getTextFromElement(doc.select(addressClass))
                .replace(" На карте", "");

        pattern = Pattern.compile(regexAddress);
        matcher = pattern.matcher(addressText);
        if (matcher.matches()) {
            district = matcher.group(1);
            street = matcher.group(2);
            house = matcher.group(3);
        }
        if (type.equals("квартира")) {
            roomNumber = getIntegerValue(getTextFromXpath(roomNumberXpath));
            area = getDoubleValue(getTextFromXpath(areaXpath));
        } else {
            roomNumber = -1;
            area = getDoubleValue(getTextFromXpath(roomNumberXpath));
        }
        kitchenArea = getDoubleValue(getTextFromXpath(kitchenAreaXpath));
        year = getIntegerValue(getTextFromXpath(yearXpath));
        if (year < 1000) {
            year += 1900;
        }
        floorNumbers = getIntegerFromString(getStringValue(getTextFromXpath(floorNumbersXpath)));
        if (floorNumbers.size() > 1) {
            floor = floorNumbers.get(0);
            floorsNumber = floorNumbers.get(1);
        }

        Contact contact = new Contact();
        contact.setPhoneNumber(contactPhoneNumber);
        contact.setName(fio);
        applicationContext.getBean(ContactDao.class).save(contact);

        Address address = new Address();
        address.setYear(year);
        address.setFloorNumber(floorsNumber);
        address.setDistrict(district);
        address.setStreet(street);
        address.setHouse(house);
        applicationContext.getBean(AddressDao.class).save(address);

        Offer offer = new Offer();
        offer.setAddress(address);
        offer.setArea(area);
        offer.setContact(contact);
        offer.setFloor(floor);
        offer.setFullDescription(fullDescription);
        offer.setPrice(price);
        offer.setType(type);
        offer.setKitchenArea(kitchenArea);
        offer.setRoomNumber(roomNumber);
        offer = applicationContext.getBean(OfferDao.class).saveIfNotExists(offer);


        ArrayList<String> xpathImageButton = new ArrayList<>() {
            {
                add(imagesButtonXpath);
            }
        };
        imageNumber = getIntegerFromString(getStringValue(getTextFromXpath(imagesButtonXpath)));
        doc = documentFromUrl(url, xpathImageButton);
        if (imageNumber.size() > 0) {
            Image image = new Image();
            for (int i = imageNumber.get(0); i > 0; i--) {
                doc = getNextImagePage();
                imageXpath = String.format(imagePatternXpath, i);
                imageUrl = getStringValue(getAttrFromXpath(imageXpath, "src"));
                if (!Objects.equals(imageUrl, defaultString)) {
                    image = new Image();
                    image.setImageUrl(imageUrl);
                    image.setOffer(offer);
                    applicationContext.getBean(ImageDao.class).save(image);
                }
            }
            offer.setMainImage(image.getImageUrl());
            applicationContext.getBean(OfferDao.class).save(offer);
        }
    }


    private Document getNextImagePage() {
        try {
            applicationContext.getBean(WebDriver.class).findElement(By.xpath(prevImageButtonXpath)).click();
        }
        catch (Exception exception) {
            etagiLogger.error("Next image button not found...");
        }
        String ps = applicationContext.getBean(WebDriver.class).getPageSource();
        return Jsoup.parse(ps);
    }


    private int getNumberOfPages() {
        int pageNumber = 0;

        //Detect the number of pages
        Elements inputElements = doc.getElementsByTag("button");

        for (Element inputElement : inputElements) {
            if (inputElement.className().equals(pagesNumberClass)) {
                if (onlyDigits(inputElement.text())) {
                    pageNumber = Integer.parseInt(inputElement.text());
                }
            }
        }

        return pageNumber;
    }
}
