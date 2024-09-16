package com.gildedrose

class GildedRose(var items: Array<Item>) {

    fun updateQuality() {
        //use immutability as much as possible: we are not allowed to change signature of the function
        //but we can still create new items instead of update in place
        for (i in items.indices) {
            items[i] = ItemProcessor.process(items[i])
        }
    }
}

