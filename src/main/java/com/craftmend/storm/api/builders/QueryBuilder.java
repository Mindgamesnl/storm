package com.craftmend.storm.api.builders;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.enums.Order;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.parser.objects.ParsedField;
import com.craftmend.storm.parser.objects.RelationField;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class QueryBuilder<T extends StormModel> {

    @Getter private Class<T> model;
    @Getter private ModelParser<T> parser;
    private Integer limit = null;
    private List<WhereClause> whereClauseList = new ArrayList<>();
    private OrderCause order = null;
    private Storm storm;
    private boolean or = false;

    /**
     * Initiate a new empty query builder
     * @param modelClass Type class
     * @param parser Parsed type class
     */
    public QueryBuilder(Class<T> modelClass, ModelParser<T> parser, Storm storm) {
        this.model = modelClass;
        this.parser = parser;
        this.storm = storm;
    }

    /**
     * Limit the result to be X max
     *
     * @param limit Limit count
     * @return self
     */
    public QueryBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Order results by a certain column
     *
     * @param column Column to order by
     * @param order Sort order
     * @return self
     */
    public QueryBuilder<T> orderBy(String column, Order order) {
        this.order = new OrderCause(column, order);
        return this;
    }

    /**
     * Defines that the next parameter should be an or
     *
     * @return self
     */
    public QueryBuilder<T> or() {
        this.or = true;
        return this;
    }

    /**
     * Add a new where cause
     *
     * @param column Column to target
     * @param comparison Comparison rule to match
     * @param value Matcher
     * @return self
     */
    public QueryBuilder<T> where(String column, Where comparison, Object value) {
        this.whereClauseList.add(new WhereClause(column, comparison, value));
        return this;
    }

    /**
     * Build a new query
     * @return Query
     */
    public PreparedQuery build() {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + parser.getTableName() + "");
        LinkedList<Object> values = new LinkedList<>();

        for (int i = 0; i < this.whereClauseList.size(); i++) {
            WhereClause wc = whereClauseList.get(i);
            boolean isLast = i+1 == whereClauseList.size();

            ParsedField f = parser.fieldByColumnName(wc.column);
            if (f == null) throw new IllegalArgumentException("there's no column called " + wc.column + " in this model");

            if (i == 0) {
                sql.append(" WHERE ");
            } else {
                sql.append(" ");
            }

            sql.append(wc.column)
                    .append(" ")
                    .append(wc.comparison.getSqlOp())
                    .append(" ?"); // polyfill with f.toSqlStringType(value)
            values.add(wc.value);
            if (!isLast) {
                if (or) {
                    or = false;
                    sql.append(" OR");
                } else {
                    sql.append(" AND");
                }
            }
        }

        if (this.order != null) {
            ParsedField f = parser.fieldByColumnName(order.column);
            if (f == null) throw new IllegalArgumentException("there's no column called " + order.column + " in this model");
            sql.append(" ORDER BY " + this.order.column + " " + this.order.order.toString());
        }

        if (this.limit != null) {
            sql.append(" LIMIT " + this.limit);
        }

        Object[] preparedV = new Object[values.size()];
        for (int i = 0; i < values.size(); i++) {
            preparedV[i] = values.get(i);
        }

        PreparedQuery pq = new PreparedQuery();
        pq.query = sql.toString();
        pq.values = preparedV;

        return pq;
    }

    /**
     * Execute the query! go brr!
     * @return Promise of results
     */
    public CompletableFuture<Collection<T>> execute() throws Exception {
        return this.storm.executeQuery(this);
    }

    @AllArgsConstructor
    private class OrderCause {
        String column;
        Order order;
    }

    @AllArgsConstructor
    private class WhereClause {
        String column;
        Where comparison;
        Object value;
    }

    @Getter
    public class PreparedQuery {
        String query;
        Object[] values;
    }

}
