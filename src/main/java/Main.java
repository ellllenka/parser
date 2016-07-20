import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String JS_CALL_HOME = "showTS('A',1)";
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setCssErrorHandler(new SilentCssErrorHandler());

        //стандартные настройки просто нашел в интернете менять не надо
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setAppletEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setPopupBlockerEnabled(true);
        webClient.getOptions().setTimeout(10000);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(true);
        webClient.waitForBackgroundJavaScript(5000);

        //Date date;
        Calendar c = Calendar.getInstance();; //System.out.println(c.get(Calendar.YEAR));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        List<Category> category1 = new ArrayList<Category>();
        List<Category> category2 = new ArrayList<Category>();




        try {
            HtmlPage page = webClient.getPage("http://live2.7msport.com/"); // заходим на сайт
            //находим все теги вида <a class='bflk' ...> c помощью XPath выражения "//a[@class='bflk']"
            //XPath - это некий язык запросов к xml/html документам.
            List<HtmlAnchor> scores = ((List<HtmlAnchor>) page.getByXPath( "//a[@class='bflk']"));

            //обходим в цикле все ссылки и достаем ID матча из JS функции вида "Javascript:ShowDetails_en(id)"
            for (HtmlAnchor score : scores) {
                String id = score.getHrefAttribute().replace("javascript:ShowDetails_en(","").replace(")","");
                //как я заметил страница аналитики по матчу имеет вид "http://analyse.7msport.com/"+id+"index.shtml"
                //System.out.println("http://analyse.7msport.com/"+id+"index.shtml");
                //теперь нужно перейти на эту страницу подобно тому как мы вошли на основную и дальше исктаь как достать инфу
                //пользуйся хромом что бы просматривать структуру html и найти за чт зацеппится
                Integer sumZero1stTime1 = 0;

                HtmlPage page1 = webClient.getPage("http://analyse.7msport.com/"+id+"/index.shtml");
                //http://analyse.7msport.com/1595010/index.shtml
                List<HtmlTableRow> scoresRow = ((List<HtmlTableRow>) page1.getByXPath("//Table[@class='qdwj1']//tr"));  // 1-й шаг
                for (HtmlTableRow sr: scoresRow) {
                    if (!sr.getAttribute("class").equals("sjt1") && !sr.getAttribute("class").equals("sjt2") && !sr.getAttribute("class").equals("sjt3") && !sr.getAttribute("class").equals("sjt4"))
                        continue;
                    c.setTime(formatter.parse(sr.getCell(1).asText()));
                    //System.out.println(c.get(Calendar.YEAR));

                    if (c.get(Calendar.YEAR) >= (c.get(Calendar.YEAR)-3)){ //исключили матчи старше 3 лет
                        if (!sr.getCell(3).asText().equals("0-0")){ //исключили общ.результат 0-0
                            if (sr.getCell(9).asText().equals("0-0")) {      //выбрали td без класса
                                sumZero1stTime1++;
                            }
                            if (sumZero1stTime1 > 1){
                                System.out.println("игра " + sr + " попадает в I категорию");
                            }
                            else if (sumZero1stTime1 <= 1){
                                System.out.println("игра " + sr + " попадает вo II категорию");
                            }

                        }
                    }
                }
                Integer sumZeroTotal1 = 0;
                Integer sumZeroHome = 0;
                List<HtmlTableRow> scoresRowTotal1 = ((List<HtmlTableRow>) page1.getByXPath("//Table[@id='tbTeamHistory_A_all']//tr")); // 2-й шаг
                ScriptResult result = page.executeJavaScript(JS_CALL_HOME);
                List<HtmlTableRow> scoresRowHome = ((List<HtmlTableRow>) page1.getByXPath("//Table[@id='tbTeamHistory_A_home']//tr")); //????????????? only 2 components
                for (HtmlTableRow srt: scoresRowTotal1){
                    if (!srt.getAttribute("class").equals("sjt1") && !srt.getAttribute("class").equals("sjt2") && !srt.getAttribute("class").equals("sjt3") && !srt.getAttribute("class").equals("sjt4"))
                        continue;
                    c.setTime(formatter.parse(srt.getCell(1).asText()));
                    if (c.get(Calendar.YEAR) >= (c.get(Calendar.YEAR)-3)){
                        if (srt.getCell(3).asText().equals("0-0")) {
                            sumZeroTotal1++;
                        }
                    }
                }
                for (HtmlTableRow srh: scoresRowHome) {
                    if (!srh.getAttribute("class").equals("sjt1") && !srh.getAttribute("class").equals("sjt2"))
                        continue;
                    c.setTime(formatter.parse(srh.getCell(1).asText()));
                    if (c.get(Calendar.YEAR) >= (c.get(Calendar.YEAR)-3)){

                    }

                }
                Integer sumZeroTotal2 = 0;
                List<HtmlTableRow> scoresRowTotal2 = ((List<HtmlTableRow>) page1.getByXPath("//Table[@id='tbTeamHistory_B_all']//tr")); // 2-й шаг
                List<HtmlTableRow> scoresRowAway = ((List<HtmlTableRow>) page1.getByXPath("//Table[@id='tbTeamHistory_B_away']//tr")); //????????????? only 2 components
                for (HtmlTableRow srt: scoresRowTotal2){
                    if (!srt.getAttribute("class").equals("sjt1") && !srt.getAttribute("class").equals("sjt2") && !srt.getAttribute("class").equals("sjt3") && !srt.getAttribute("class").equals("sjt4"))
                        continue;
                    c.setTime(formatter.parse(srt.getCell(1).asText()));
                    if (c.get(Calendar.YEAR) >= (c.get(Calendar.YEAR)-3)){
                        if (srt.getCell(3).asText().equals("0-0")) {
                            sumZeroTotal2++;
                        }
                    }
                }
                for (HtmlTableRow srh: scoresRowHome) {
                    if (!srh.getAttribute("class").equals("sjt1") && !srh.getAttribute("class").equals("sjt2") && !srh.getAttribute("class").equals("sjt3") && !srh.getAttribute("class").equals("sjt4"))
                        continue;
                    c.setTime(formatter.parse(srh.getCell(1).asText()));
                    if (c.get(Calendar.YEAR) >= (c.get(Calendar.YEAR)-3)){

                    }

                }




            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

/*
results = page.getByXPath( "//tr")
for (res in results){
   res.getByXPath( "//td[@class='td_time']") // тут дата
   res.getByXPath( "//td[@class='td_score']") //тут счет по этой строке
}

<a href="javascript:Team_en(2874)" style="color:#ff0000">Latvia(U17)</a>
 */