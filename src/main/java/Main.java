import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {
    public static void main(String[] args) {
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

        Calendar c = new GregorianCalendar(); //System.out.println(c.get(Calendar.YEAR));

        List<Category> category1 = new ArrayList<Category>();
        List<Category> category2 = new ArrayList<Category>();

        Integer sumZero1stTime = 0;


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
                HtmlPage page1 = webClient.getPage("http://analyse.7msport.com/"+id+"/index.shtml");
                //http://analyse.7msport.com/1595010/index.shtml
                List<HtmlTableRow> scoresRow = ((List<HtmlTableRow>) page1.getByXPath("//Table[@class='qdwj1']//tr"));
                for (HtmlTableRow sr: scoresRow) {
                    if (!sr.getAttribute("class").equals("sjt1") && !sr.getAttribute("class").equals("sjt2"))
                        continue;
                    String[] date = ((HtmlTableDataCell) sr.getByXPath("td[@class='td_time']").get(0)).asText().split("/");

                    if (Integer.parseInt(date[date.length-1]) >= (c.get(Calendar.YEAR)-2000 -3)){ //исключили матчи старше 3 лет
                        if (!((HtmlTableDataCell) sr.getByXPath("td[@class='td_score']").get(0)).asText().equals("0-0")){ //исключили общ.результат 0-0
                            if (sr.getCell(9).asText().equals("0-0")) {      //выбрали td без класса
                                sumZero1stTime++;
                            }
                            if (sumZero1stTime > 1){
                                System.out.println(sr);
                            }

                        }
                    }
                }
                List<HtmlTableRow> scoresRow2 = ((List<HtmlTableRow>) page1.getByXPath("//Table[@class='zrqt1']//tr"));
                for (HtmlTableRow sr: scoresRow2) {
                    if (!sr.getAttribute("class").equals("sjt3") && !sr.getAttribute("class").equals("sjt4"))
                        continue;
                    String[] date = ((HtmlTableDataCell) sr.getByXPath("td[@class='d1']").get(0)).asText().split("/");
                    if (Integer.parseInt(date[date.length-1]) >= (c.get(Calendar.YEAR)-2000 -3)){
                        
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