package com.ubfc.MobilityMiniProject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    int value;
    double distance;
    Node father;
    Node left;
    Node right;
    Node middle;

    Node(int value, double distance, Node father) {
        this.value = value;
        this.distance = distance;
        this.father = father;
        right = null;
        left = null;
        middle = null;
    }

    public Node insert(int value, double distance){
        if(this.father != null) {
            if (value == this.father.value) {
                return null;
            }
        }
        if (left == null){
            left = new Node(value, distance, this);
            return left;
        }else if (right == null){
            right = new Node(value, distance, this);
            return right;
        }else if (middle == null){
            middle = new Node(value, distance, this);
            return middle;
        }else{
            // ?? insert child ??
        }
        return null;
    }

    public void print(){
        Log.d("VALUE : ", String.valueOf(value));
        Log.d("DISTANCE : ", String.valueOf(distance));
        if (this.father != null) Log.d("FATHER : ", String.valueOf(father.value));
        Log.d("LEFT : ", String.valueOf(left));
        Log.d("RIGHT : ", String.valueOf(right));
        Log.d("MIDDLE : ", String.valueOf(middle));
        if (left != null) left.print();
        if (right != null) right.print();
        if (middle != null) middle.print();
    }

    public Node[] getChildren(){
        Node[] res = {};
        for (Node child : new Node[]{left, right, middle}){
            if(child !=null) {
                res = Arrays.copyOf(res, res.length + 1);
                res[res.length - 1] = child;
            }
        }
        return res;
    }

    public void printAncestors(){
        Log.d("(ANCESTOR) VALUE : ", String.valueOf(value));
        if (this.father != null){
            father.printAncestors();
        }
    }

    public ArrayList<Node> getAncestors(ArrayList<Node> incoming){
        if (incoming == null){
            incoming = new ArrayList<Node>();
        }
        incoming.add(0,this);
        if(this.father != null){
            return this.father.getAncestors(incoming);
        }
        return incoming;
    }
}