package web.group05.trentoticketing.Helper;

import web.group05.trentoticketing.Data.CartItem;
import web.group05.trentoticketing.Data.User;

import javax.servlet.http.HttpSession;

public class HtmlHelper {
    public static String getHeader(HttpSession session, String title) {
        return getHeader(session, title, "", "");
    }
    public static String getHeader(HttpSession session, String title, String style) { return getHeader(session, title, style, ""); }

    public static String getHeader(HttpSession session, String title, String style, String script) {
        User user = session != null ? (User)session.getAttribute("user") : null;
        CartItem[] cart = session != null ? (CartItem[])session.getAttribute("cart") : null;
        int items = 0;
        if (cart != null) {
            for (CartItem item : cart) {
                items += item.qty;
            }
        }

        return "<!DOCTYPE html>" +
                "<html lang=\"it\">" +
                "<head>" +
                "<title>" + title +"</title>" +
                "<link rel=\"icon\" type=\"image/x-icon\" href=\"images/favicon.ico\">" +
                "<link rel='stylesheet' type='text/css' media='screen' href='css/main.css'>" +
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">\n" +
                "    <style>" + style + "</style>\n" +
                script +
                "</head><body style=\"min-height: 100vh;\">" +
                "<nav class=\"navbar navbar-expand-lg navbar-light bg-light\">\n" +
                "       <div id=\"cookie-banner\" class=\"cookie-banner\">\n" +
                "           Questo sito utilizza i cookie per migliorare l'esperienza di navigazione. Procedendo acconsenti al loro utilizzo.\n" +
                "           <button class='btn btn-success' onclick=\"acceptCookies()\">Ok</button>\n" +
                "       </div>" +
                "        <div class=\"container-fluid\">\n" +
                "            <a class=\"navbar-brand\" href=\"Home\">Home</a>\n" +
                "          <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "            <span class=\"navbar-toggler-icon\"></span>\n" +
                "          </button>\n" +
                "          <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n" +
                "                <ul class=\"navbar-nav me-auto mb-2 mb-lg-0\">\n" +
                "                    <li class=\"nav-item dropdown\">\n" +
                "                        <a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"navbarDropdown\" role=\"button\"\n" +
                "                            data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\n" +
                "                            Eventi\n" +
                "                        </a>\n" +
                "                        <ul class=\"dropdown-menu\" aria-labelledby=\"navbarDropdown\">\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=0\">Concerti</a></li>\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=1\">Spettacoli Teatrali</a></li>\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=2\">Eventi Sportivi</a></li>\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=3\">Visite Guidate</a></li>\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=4\">Mostre</a></li>\n" +
                "                            <li><a class=\"dropdown-item\" href=\"EventCategoryList?type=5\">Musei</a></li>\n" +
                "                        </ul>\n" +
                "                    </li>\n" +
                "                    <li class=\"nav-item\"><a class=\"nav-link\"  href=\"Cart\">Carrello (" + items +")</a></li>\n" +
                (user != null ? (user.getIs_admin() ?
                "                    <li class=\"nav-item\"><a class=\"nav-link\" href=\"SeeUsers\">Visualizza Utenti</a></li>\n" +
                "                    <li class=\"nav-item\"><a class=\"nav-link\" href=\"HandleEvents\">Gestisci Eventi</a></li>\n"
                :
                "                    <li class=\"nav-item\"><a class=\"nav-link\" href=\"UserProfile\">Profilo</a></li>\n"
                ):"") +
                "                </ul>\n" +
                "           <div>\n" +
                (user == null ?
                "               <a class=\"btn btn-primary\" href=\"singup.html\">Sing Up</a>\n" +
                "               <a class=\"btn btn-primary\" href=\"login.html\">Login</a>"
                :
                                "<a class=\"btn btn-primary\" href=\"Logout\">Logout</a>\n") +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav><main>";
    }

    public static String getFooter() {
        return getFooter("");
    }
    public static String getFooter(String js) {
        return "</main>" +
                "<script>\n" + js + "\n</script>"+
                "<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM\" crossorigin=\"anonymous\"></script>" +
                "<script src='js/main.js'></script>" +
                "</body></html>";
    }
}
