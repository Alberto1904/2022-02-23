/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Migliori;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnMiglioramento"
    private Button btnMiglioramento; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiLocali(ActionEvent event) {
    	this.cmbLocale.getItems().clear();
    	String citta = this.cmbCitta.getValue();
    	if(citta == null) {
    		//TODO popolare la tendina dei locali per la città selezionata
    		txtResult.appendText("INSERIRE UNA CITTA");
    		return;
    	}
    this.cmbLocale.getItems().addAll(this.model.getLocali(citta));
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	String citta=this.cmbCitta.getValue();
    	Business b=this.cmbLocale.getValue();
    	if(b==null||citta==null)
    	{
    		txtResult.appendText("INSERIRE UNA CITTA E UN LOCALE");
    		return;
    	}
    	this.model.creaGrafo(b, citta);
    	txtResult.appendText("NVERTICI:"+this.model.nVertici()+"\n");
    	txtResult.appendText("NARCHI:"+this.model.nArchi()+"\n");
    	List<Migliori> m= this.model.getMigliore();
    	for(Migliori m1: m)
    	{
    		txtResult.appendText(m1.toString()+"\n");
    	}
    	
    }

    @FXML
    void doTrovaMiglioramento(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.getGrafo()==false)
    	{
    		txtResult.appendText("CREA IL GRAFO");
    		return;
    	}
    List<Review> best=	this.model.cerca();
   LocalDate data1=best.get(0).getDate();
   LocalDate data2=best.get(best.size()-1).getDate();
   double peso=ChronoUnit.DAYS.between(data1, data2);
   if(peso<0)
	   peso=-1*peso;
	for(Review m1: best)
	{
		txtResult.appendText(m1.toString()+"\n");
	}
	txtResult.appendText(peso+"\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMiglioramento != null : "fx:id=\"btnMiglioramento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCitta());
    }
}
