package viktoriia.vihriian.cocktailgod;

/**
 * private final String[] sort = {"По имени", "По ингредиенту"};
 private final String[] category         =   {"Не выбрано", "Шоты", "Лонги", "Тини", "Горячие"};
 private final String[] mainIngredient   =   {"Не выбрано", "Водка", "Ром", "Виски", "Вино", "Пиво",
 "Портвейн", "Коньяк", "Текила"};
 private final String[] strength         =   {"Не выбрано", "Безалкогольный", "Слабоалкогольный", "Крепкий"};
 private final String[] difficulty       =   {"Не выбрано", "Легко", "Средне", "Сложно"};
 private final String[] taste          =   {"Не выбрано", "Кислосладкие", "Кислые", "Сладкие", "С горчинкой", "Пряные",
 "Острые", "Фруктовые", "Ягодные", "Сливочные"};
 */
public class Filter {
    private static Filter filterInstance;
    final static String FILTER_BY_NAME = "По имени";
    final static String FILTER_BY_INGREDIENTS = "По ингредиенту";
    protected static String[] currentFilters = {"По имени", "Не выбрано", "Не выбрано", "Не выбрано",
            "Не выбрано", "Не выбрано"};
    protected static String[] titleArray = {"Поиск", "Категория", "Основной ингредиент", "Крепость",
            "Cложность", "Вкус"};

    private Filter() {
    }

    public static Filter getInstance() {
        if(filterInstance == null)
            filterInstance = new Filter();
        return filterInstance;
    }

    protected static void setCurrentFilterItem(String text, int pos) {
        currentFilters[pos] = text;
    }
}
