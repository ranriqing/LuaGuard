/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */

package luaguard.obfuscator;

import java.util.HashMap;
import java.util.List;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Exp.NameExp;
import org.luaj.vm2.ast.Exp.VarExp;
import org.luaj.vm2.ast.Name;
import org.luaj.vm2.ast.Stat.Assign;
import org.luaj.vm2.ast.Stat.LocalAssign;

/**
 * Obfuscator to modify the variables
 * @author Hanqing & Will
 */
public class VarRenamerObfuscator extends Obfuscator{
    //function name dictionary
    private HashMap<String, String> dict;
    
    /**
     * Constructor of the variable renamer obfuscator
     */
    public VarRenamerObfuscator(){
        dict = new HashMap<String, String>();
    }
    /**
     * Constructor of the variable renamer obfuscator
     * @param blacklist
     */
    public VarRenamerObfuscator(List<String> blacklist){
        dict = new HashMap<String, String>();
        for (String blacklist1  : blacklist) {
            dict.put(blacklist1 , blacklist1 );
        }
    }
	
    /**
     * Ignore name convert
     * local bcd;
     * abc.bcd or abc.bcd();
     * patterns code
     * @param var1
     */
    public void visit(Exp.FieldExp var1) {
        var1.lhs.accept(this);
        if(var1.isvarexp()){
            // do nothing
        }else{
            this.visit(var1.name);
        }
    }

    /**
     * If meet a new name than check its name in the hashmap, if it exists,
     * then modify it.
     * @param name ast name node
     */
    @Override
    public void visit(Name name) {
        if(name == null) return;
        String oldName = name.name;
        String tempname = null;
        //if the name is in dictionary
        if(dict.containsKey(oldName)){
            tempname = dict.get(oldName);
            name.name = tempname;
        }else{
            dict.put(oldName,oldName);
        }
        /*if(name.variable != null)
            name.variable.name = tempname;*/
    }
    /**
     * Rename the variable name in local assignment
     * 
     * @param la LocalAssign, the ast node object
     */
    @Override
    public void visit(LocalAssign la){
        //NameResolver nsolver = new NameResolver();
        if(la == null) return;
        //variable
        for(int j = 0; j < la.names.size(); j++){
            String oldName = ((Name)(la.names.get(j))).name; 
            String tempname;
            //if the name is in dictionary
            if(dict.containsKey(oldName)){
                tempname = dict.get(oldName);
            }else{
                //create new entry
                tempname = (new RandomName()).generateVarName(this.dict);
                //put the new hashmap entry
                dict.put(oldName, tempname);
            }
            ((Name)(la.names.get(j))).name = tempname;
        }
        //check the right side
        if (null != la.values ) {
            // Expressions
            for (int i = 0; i < la.values.size(); i++) {
                ((Exp) la.values.get(i)).accept(this);
            }
        }
    }
    
    /**
     * Rename the assign
     * 
     * @param as Assign, the ast node object for assignment
     */
    
    @Override
    public void visit(Assign as){
        
        for(int i =0; i< as.vars.size(); i++){
            if(as.vars.get(i).toString().contains("$NameExp")){
                NameExp name = (NameExp) as.vars.get(i);
                String oldname = name.name.name;
                String tempname;
                if(dict.containsKey(oldname)){
                    tempname = dict.get(oldname);
                }
                else{
                    tempname = oldname;
                    dict.put(oldname,tempname);
                }
                name.name.name = tempname;
            }
        }
        visitVars(as.vars);
	visitExps(as.exps);
    }
}
