package vttp.batch5.ssf.day16.models;

/*
 * records are immutable data classes that require only the type and name of fields
 * hold data, such as database results, query results, or information from a service
 */
public record SearchParams(String query, int limit, String rating){

}