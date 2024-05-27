package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{
    double dist_oiseau;

    public LabelStar(Node sc, boolean m, double cr,Arc p,Node sp,double dist_o){
        super(sc,m,cr,p,sp);
        this.dist_oiseau = dist_o;
    }

    public double getTotalCost(){
        return this.getCout_realise()+this.dist_oiseau;
    }

}