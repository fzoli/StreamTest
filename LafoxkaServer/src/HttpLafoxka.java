
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpLafoxka {

    private final HttpExecutor EXECUTOR;
    private final Pattern P = Pattern.compile("(<td.*?>)(.*?)(</td><td.*?>)(.*?)</td>");
    
    public HttpLafoxka(String bejeaz, String embazon) {
        EXECUTOR = new HttpExecutor("http://www.lafoxka.hu/beszelget.php?bejeaz=" + bejeaz + "&embazon=" + embazon, "iso8859-2");
    }
    
    public List<String> send(String message) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("valasz", message);
        String src = EXECUTOR.getResponse(map);
        /*String src = "</table>   "+
"              <div align=\"center\"></div><table width=\"95%\" border=\"1\" cellspacing=\"2\" cellpadding=\"2\" bgcolor=\"#CCCCCC\" align=\"center\"><tr><td width=\"12%\">Lafoxka</td><td width=\"88%\">Kár, hogy nem akarod a tesztet megcsinálni. Ha később meggondolnád magad, akkor szóljál!</td></tr></table><table width=\"95%\" border=\"1\" cellspacing=\"2\" cellpadding=\"2\"><tr><td> Te </td><td>vége</td></tr></table><div align=></div><table width=><tr><td widt>Lafoxka</td><td>10 kérdést fogok feltenni. A kiválasztott teszt első 10 kérdését. Kérlek add meg annak a tesztek a számát, amit ki akarsz tölteni! Ha meggondoltad magad, írd be: vége</td></tr></table><div align></div><table wid><tr><td width>Lafoxka</td><td wi><table border=0><tr><td>Teszt száma </td><td>Teszt témája (LAFOX tesztek mindenkinek) </td></tr><tr><td>1</td><td>Tudáspróba 1.</td></tr><tr><td>2</td><td>Kényszeres-e Ön?</td></tr><tr><td>3</td><td>Optimizmus-pesszimizmus.</td></tr><tr><td>4</td><td>Teszt az őszinteségről.</td></tr><tr><td>5</td><td>Alkohol teszt.</td></tr><tr><td>6</td><td>Földrajz.</td></tr></table></td></tr></table><table widorn><tr><td width=> Te </td><td width>igen</td></tr></table><div al></div><table wid><tr><td w>Lafoxka</td><td wi>Ha akarod, akkor kitölthetsz egy tesztet. Megnézhetjük, hogy te milyen okos vagy. Rendben? Kérlek igennel válaszolj, ha akarod!</td></tr></table><div alig></div><table w alig><tr><td widt>Lafoxka</td><td widt>A LAFOX tesztek mindenkinek sorozatban több ezer teszt kérdést találsz. Igazi karácsonyi ajándéknak való program. Érdemes megnézned a részleteket a LAFOX weblapján!</td></tr></table><table wi cellpadd bgc al><tr><td > Te </td><td widt>teszt</td></tr></table><div alig></div><table wid ali><tr><td wi>Lafoxka</td><td widt>Ó... Ezek a csodás számok! Ez vagyok én. Persze nem egy, nulla, nullla, egy hanem valahogy így: 11010111 11110101 111101000.</td></tr></table><table widt cells><tr><td wid> Te </td><td wid>kettő</td></tr></table><div ali></div><table widt borlign=><tr><td widt>Lafoxka</td><td widt>Most ez vagy túl egyszerű volt, vagy túl bonyolult. Egyiket se szeretem... Megpróbálnád még egyszer?</td></tr></table><table widellpadd bgcoloalign><tr><td wi> Te </td><td wid>egy</td></tr></table><div alig></div><table widtolor=al><tr><td width>Lafoxka</td><td>Üdvözöllek farcsal Sikerült a regisztrálásod. Az azonosítód: 51312<br>Most Lafoxkával beszélsz.<br>Egy program, aki tud beszélni.<br>Ha írni akarsz az email címem: lafoxka@lafoxka.hu.<br>Még csak 3463 napja születtem, ezért nem tudok sokat.<br>Kérlek figyelj arra, hogy ne kövess el helyesírási vagy gépelési hibát! Csak akkor értelek meg, ha magyar ékezetes betűket használsz.<br>Kérlek ne írj csupa nagybetűvel, mert azt nem biztos hogy megértem. A mondat elején természetesen haszhálhatsz nagybetűt.<br>Ha be akarod fejezni a beszélgetést, kérlek írj egy pontot (csak egy pontot) a válaszodba!</td></tr></table>            </td>"+
"          </tr>"+
"        </table>"+
"      </div>";*/
        Matcher m = P.matcher(src);
        ArrayList<String> msgs = new ArrayList<String>();
        while (m.find()) {
            if (!m.group(2).equals("Lafoxka")) break;
            String s = m.group(4).replaceAll("<br>", "\n");
            s = s.replaceAll("<p>", "");
            
            if (s.startsWith("<table")) {
                String tmp = src.substring(src.indexOf(s));
                tmp = tmp.substring(tmp.indexOf(">") + 1, tmp.indexOf("</table>"));
                tmp = tmp.replaceAll("<tr>", "\n");
                tmp = tmp.replaceAll("</tr>", "");
                tmp = tmp.replaceAll("</td>", "");
                tmp = tmp.replaceAll("<td>", "    ");
                tmp = tmp.replaceAll("\n    ", "\n");
                msgs.add(tmp);
            }
            else msgs.add(s);
            
            /*if (!s.startsWith("<table")) msgs.add(s);
            else {
                msgs.add("Teszt száma	Teszt témája (LAFOX tesztek mindenkinek)\n"+
                "1	Tudáspróba 1.\n"+
                "2	Kényszeres-e Ön?\n"+
                "3	Optimizmus-pesszimizmus.\n"+
                "4	Teszt az őszinteségről.\n"+
                "5	Alkohol teszt.\n"+
                "6	Földrajz.");
            }*/
            
        }
        Collections.reverse(msgs);
        return msgs;
    }
    
}