package com.acktos.easylaundry;

import android.provider.BaseColumns;


public final class LaundryContract {


    public LaundryContract(){

    }

    public static abstract class ClothesTable implements BaseColumns {
        public static final String TABLE_NAME = "clothes";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";

    }

    public static abstract class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_THUMBNAIL = "thumbnail";

    }
}
