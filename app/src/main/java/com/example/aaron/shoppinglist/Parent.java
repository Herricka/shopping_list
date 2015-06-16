package com.example.aaron.shoppinglist;

import java.util.ArrayList;

public class Parent {
    private String name_parent;
    private String text1_parent;
    private String checkedtype;
    private String quantity_parent;
    private boolean checked_parent;

    //ArrayList to store child objects
    private ArrayList<Child> children_array;

    public ArrayList<Child> getChildren() {
        return children_array;
    }

    public void setChildren(ArrayList<Child> pChildren) {
        this.children_array = pChildren;
    }

    //Getters and setters for Parent values

    public String getName() {
        return name_parent;
    }

    public void setName(String pName) {
        this.name_parent = pName;
    }

    public String getText1() {
        return text1_parent;
    }

    public void setText1(String pText1) { //Product name in grocery list screen
        this.text1_parent = pText1;
    }

    public String getQuantity() {
        return quantity_parent;
    }

    public void setQuantity(String pQuantity) {
        this.quantity_parent = pQuantity;
    }


    public String getCheckedType() {
        return checkedtype;
    }

    public void setCheckedType(String pCheckedType) {
        this.checkedtype = pCheckedType;
    }

    public boolean isChecked() {
        return checked_parent;
    }

    public void setChecked(Boolean pIsChecked) {
        this.checked_parent = pIsChecked;
    }



}
