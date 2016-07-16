/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.edu.anu.cecs.explorer;

/**
 *
 * @author anila
 */
public class Relation {

    String property;
    String propertyLabel;
    String value;
    String valueLabel;

    /******************** Constructor *************************/
    public Relation(){
        property = "";
        propertyLabel="";
        value = "";
        valueLabel="";
    }

    /******************** Setter Function *************************/

    public void setProperty(String prop) {
        property = prop; }
    
    public void setPropertyLabel(String proplabel) {
        propertyLabel = proplabel; }

    public void setValue(String val) {
        value = val; }

    public void setValueLabel(String valLab) {
        valueLabel = valLab; }


    /******************** Getter Function *************************/

    public String getProperty() {
        return this.property; }

    public String getPropertyLabel() {
        return this.propertyLabel; }
    
    public String getValue() {
        return this.value; }

     public String getValueLabel() {
        return this.valueLabel; }

}
