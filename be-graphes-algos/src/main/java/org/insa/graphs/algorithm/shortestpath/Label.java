package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;


public class Label implements Comparable<Label>{
    Node sommet_courant;
    boolean marque;
    double cout_realise;
    Arc pere;
    Node sommet_pere;
    

    public Label(Node sc, boolean m, double cr,Arc p,Node sp){
        this.sommet_courant = sc;
        this.marque = m;
        this.cout_realise = cr;
        this.pere = p;
        this.sommet_pere = sp;
    }

    public Node getSommet_courant() {
        return sommet_courant;
    }

    public void setSommet_courant(Node sommet_courant) {
        this.sommet_courant = sommet_courant;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setCost(double cout_realise) {
        this.cout_realise = cout_realise;
    }

    public void setPere(Arc pere) {
        this.pere = pere;
    }

    public boolean isMarque() {
        return marque;
    }

    public double getCout_realise() {
        return cout_realise;
    }

    public Arc getPere() {
        return pere;
    }

    public Node getsommetpere(){
        return sommet_pere;
    }

    public void setsommetpere(Node n){
        sommet_pere = n;
    }

    public double getTotalCost(){
        return this.getCout_realise();
    }

    public int compareTo(Label l){
        if (this.getTotalCost()<l.getTotalCost()){
            return -1;
        }
        if (this.getTotalCost()==l.getTotalCost()){
            if(this.getCout_realise()<l.getCout_realise()){
                return -1;
            } else{
                if(this.getCout_realise()==l.getCout_realise()){
                    return 0;
                } else {
                    return 1;
                } 
            }  
            
            
        }
        return 1;
    }


} 