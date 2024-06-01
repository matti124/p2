package it.unisa.control;

import java.io.IOException; 
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.*;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDao prodDao;

    public CatalogoServlet() {
        super();
        prodDao = new ProdottoDao();
    }

    @SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String redirectedPage= request.getParameter("page");
        String sort=request.getParameter("sort");

        try {
            if (action != null) {
                if (action.equalsIgnoreCase("add")) {
                    // Validazione dei dati in ingresso
                    String nome = request.getParameter("nome");
                    String descrizione = request.getParameter("descrizione");
                    String iva = request.getParameter("iva");
                    String prezzoStr = request.getParameter("prezzo");
                    String quantitaStr = request.getParameter("quantità");
                    String piattaforma = request.getParameter("piattaforma");
                    String genere = request.getParameter("genere");
                    String immagine = request.getParameter("img");
                    String dataUscita = request.getParameter("dataUscita");
                    String descDett = request.getParameter("descDett");

                    if (nome != null && !nome.isEmpty() &&
                    	    descrizione != null && !descrizione.isEmpty() &&
                    	    iva != null && !iva.isEmpty() &&
                    	    prezzoStr != null && !prezzoStr.isEmpty() &&
                    	    quantitaStr != null && !quantitaStr.isEmpty() &&
                    	    piattaforma != null && !piattaforma.isEmpty() &&
                    	    genere != null && !genere.isEmpty() &&
                    	    immagine != null && !immagine.isEmpty() &&
                    	    dataUscita != null && !dataUscita.isEmpty()) {

                    	    double prezzo = Double.parseDouble(prezzoStr);
                    	    int quantita = Integer.parseInt(quantitaStr);

                    	    nome = StringEscapeUtils.escapeHtml4(nome);
                    	    descrizione = StringEscapeUtils.escapeHtml4(descrizione);
                    	    iva = StringEscapeUtils.escapeHtml4(iva);
                    	    piattaforma = StringEscapeUtils.escapeHtml4(piattaforma);
                    	    genere = StringEscapeUtils.escapeHtml4(genere);
                    	    immagine = StringEscapeUtils.escapeHtml4(immagine);
                    	    dataUscita = StringEscapeUtils.escapeHtml4(dataUscita);
                    	    descDett = StringEscapeUtils.escapeHtml4(descDett);

                        ProdottoBean bean = new ProdottoBean();
                        bean.setNome(nome);
                        bean.setDescrizione(descrizione);
                        bean.setIva(iva);
                        bean.setPrezzo(prezzo);
                        bean.setQuantità(quantita);
                        bean.setPiattaforma(piattaforma);
                        bean.setGenere(genere);
                        bean.setImmagine(immagine);
                        bean.setDataUscita(dataUscita);
                        bean.setDescrizioneDettagliata(descDett);
                        bean.setInVendita(true);

                        // Prevenzione di SQL injection
                        prodDao.doSave(bean);
                    } else {
                        // Gestione errori: campi obbligatori non compilati
                    }
                } else if (action.equalsIgnoreCase("modifica")) {
                    // Implementa la logica per la modifica del prodotto
                }

                request.getSession().removeAttribute("categorie");
            }


            response.sendRedirect(request.getContextPath() + "/" + redirectedPage);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel server");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}