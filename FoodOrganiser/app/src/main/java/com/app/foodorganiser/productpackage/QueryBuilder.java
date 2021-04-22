package com.app.foodorganiser.productpackage;

import com.app.foodorganiser.entity.ProductTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QueryBuilder {

    public static String buildQuery(String ... query)
    {
       StringBuilder command = new StringBuilder("");
       for(int i=0;i<query.length;i++) {
           command.append("query").append(i).append("=").append(query[i]);
           if(i != query.length-1)
               command.append("&");
       }
       return command.toString();
    }

    public static List<String> toList(String reply) {
        if(reply==null)
            return null;
        if(reply.contains("|")){
            return Arrays.asList(reply.split("\\|"));
        }
        else {
            List<String> temp = new ArrayList<>();
            temp.add(reply);
            return temp;
        }
    }

    public static String insert(ProductTable productTable, boolean constantIndex)
    {
        String query = "INSERT INTO products_table (id, name, protein, carbohydrates, fats, code, pathPhoto) VALUES (";
        if(constantIndex) {
            query+=productTable.getId()+", ";
        }
        query+="\""+productTable.getName()+"\", "+productTable.getProtein()+", "+productTable.getCarbohydrates()
                +", "+productTable.getFats()+", "+"\""+productTable.getCode()+"\""+", ";
        if(productTable.getPathPhoto()==null)
            query+="null";
        else
            query+="\""+productTable.getPathPhoto()+"\"";
        query+=");";
        return query;
    }

    public static String insert(ProductTable productTable) {
        String query = "INSERT INTO products_table (name, protein, carbohydrates, fats, code, pathPhoto) VALUES (";
        query+="\""+productTable.getName()+"\", "+productTable.getProtein()+", "+productTable.getCarbohydrates()
                +", "+productTable.getFats()+", "+"\""+productTable.getCode()+"\""+", ";
        if(productTable.getPathPhoto()==null)
            query+="null";
        else
            query+="\""+productTable.getPathPhoto()+"\"";
        query+=");";
        return query;
    }



}
