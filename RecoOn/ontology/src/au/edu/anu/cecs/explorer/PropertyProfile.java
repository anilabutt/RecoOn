/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.edu.anu.cecs.explorer;

import java.util.HashMap;

/**
 *
 * @author anila
 */

public class PropertyProfile {

    HashMap<String,String> domain = new HashMap<String,String>();
    HashMap<String,String> range = new HashMap<String,String>();

    /******************** Constructor *************************/
    public PropertyProfile(){
        domain.put("", "");
        range.put("", "");
    }

    /******************** Setter Function *************************/

    public void setDomains(HashMap<String,String> domain) {
        this.domain = domain; }

    public void setRange(HashMap<String,String> range) {
        this.range = range; }


    /******************** Getter Function *************************/
   
    public HashMap<String,String> setDomains() {
        return this.domain; }

    public HashMap<String,String> setRange() {
        return this.range; }

}
