package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	YelpDao dao;
	Map<String,Business> idMap;
	Map<String,Review> mapR;
	Graph<Review,DefaultWeightedEdge> grafo;
	List<Review> best;
	int massimo;
	public Model()
	{
		dao= new YelpDao();
		idMap= new HashMap<>();
		mapR= new HashMap<>();
		this.dao.getAllBusiness(idMap);
		this.dao.getAllReviews(mapR);
	}
	public List<String> getCitta()
	{
	return	this.dao.getCitta();
	}
	public List<Business> getLocali(String citta)
	{
		return this.dao.getLocali(citta, idMap);
	}
	public boolean getGrafo()
	{
		if(this.grafo==null)
			return false;
					return true;
	}
	
	public void creaGrafo(Business business,String citta)
	{
		grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(business, mapR, citta));
		
		List<Adiacenza> coppie=this.dao.getArchi(business, mapR, citta);
		for(Adiacenza a : coppie)
		{
			if(a.getPeso()>0)
			Graphs.addEdgeWithVertices(this.grafo, a.getR2(), a.getR1(),a.getPeso());
			else
				Graphs.addEdgeWithVertices(this.grafo, a.getR1(),a.getR2(),-1*a.getPeso());
		}
	}
	public int nVertici()
	{
		return this.grafo.vertexSet().size();
	}
	public int nArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public List<Migliori> getMigliore()
	{
		List<Migliori> migliori= new LinkedList<>();
		int max=0;
		for(Review r: this.grafo.vertexSet())
		{
			if(this.grafo.outDegreeOf(r)>max)
				max=this.grafo.outDegreeOf(r);
		}
		for(Review r: this.grafo.vertexSet())
		{
			if(this.grafo.outDegreeOf(r)==max)
			{
				Migliori m= new Migliori(r,this.grafo.outDegreeOf(r));
				migliori.add(m);
			}
				
		}
		return migliori;
	}
	public List<Review> cerca()
	{
		best= new LinkedList<>();
		List<Review> parziale= new LinkedList<>();
		massimo=0;
		Review migliore=null;
		//decido di partire dal migliore
	double min=10;
	for(Review r: this.grafo.vertexSet())
	{
		if(r.getStars()<min)
		{
			min=r.getStars();
			migliore=r;
		}
	}
		parziale.add(migliore);
		cerca_ricorsione(parziale);
		return best;
		
	}
	public void cerca_ricorsione(List<Review> parziale)
	
	{
		//condizione terminale
		if(parziale.size()>massimo)
		{
			massimo=parziale.size();
					best= new LinkedList<>(parziale);
		}
		//ricorsione
		
		for(DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(parziale.get(parziale.size()-1)))
		{
			Review r=Graphs.getOppositeVertex(this.grafo, edge,parziale.get(parziale.size()-1));
			if(!parziale.contains(r)&&r.getStars()>parziale.get(parziale.size()-1).getStars())
			{
				parziale.add(r);
				cerca_ricorsione(parziale);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}
}
