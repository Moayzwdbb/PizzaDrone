package com.ilp.pizzadrone.dto;

/**
 * defines a pizza which can be ordered from a restaurant
 * @param name is the unique (system-wide) name of a pizza
 * @param priceInPence is the price of one pizza in pence
 */
public record Pizza (String name, int priceInPence){
}
