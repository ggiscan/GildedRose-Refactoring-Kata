package com.gildedrose

sealed class ItemProcessor {
    fun processItem(item: Item): Item {
        return Item(item.name, processSellIn(item), ensureQualityBounds(processQuality(item)))
    }
    open val maxQuality = 50
    private fun ensureQualityBounds(quality: Int) = minOf(maxOf(quality, 0), maxQuality)
    abstract fun processQuality(item: Item): Int
    abstract fun processSellIn(item: Item): Int

    companion object {
        fun process(item: Item): Item {
            val processor = when(item.name) {
                "Aged Brie" -> AgedBrieProcessor
                "Sulfuras, Hand of Ragnaros" -> SulfurasProcessor
                "Backstage passes to a TAFKAL80ETC concert" -> BackstageProcessor
                "Conjured" -> ConjuredProcessor
                else -> DefaultProcessor()
            }
            return processor.processItem(item)
        }
    }
}

open class DefaultProcessor : ItemProcessor() {
    override fun processQuality(item: Item): Int = if (item.sellIn <= 0) item.quality - 2 else item.quality - 1
    override fun processSellIn(item: Item): Int = item.sellIn - 1

}

object BackstageProcessor : DefaultProcessor() {
    override fun processQuality(item: Item): Int = when {
        item.sellIn > 10 -> item.quality + 1
        item.sellIn in 6..10 -> item.quality + 2
        item.sellIn in 1..5 -> item.quality + 3
        else -> 0
    }
}

object AgedBrieProcessor : DefaultProcessor() {
    override fun processQuality(item: Item): Int = if (item.sellIn <= 0) item.quality + 2 else item.quality + 1
}

object SulfurasProcessor : ItemProcessor() {
    override val maxQuality = 80
    override fun processQuality(item: Item): Int = item.quality

    override fun processSellIn(item: Item): Int = item.sellIn
}

object ConjuredProcessor : DefaultProcessor() {
    override fun processQuality(item: Item): Int = item.quality - 2
}