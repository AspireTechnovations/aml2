package in.aspiretechnovations.aml.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import in.aspiretechnovations.aml.model.CartModel;
import in.aspiretechnovations.aml.model.MakeModel;
import in.aspiretechnovations.aml.model.ModelModel;
import in.aspiretechnovations.aml.model.ProductModel;
import in.aspiretechnovations.aml.model.VariantModel;
import in.aspiretechnovations.aml.utils.ConstantUtils;

public class GeneralTables {

    private static final String TBL_VEHICLE_MAKE = "tbl_make";
    private static final String TBL_VEHICLE_MODEL = "tbl_model";
    private static final String TBL_VEHICLE_VARIANT = "tbl_variant";

    private static final String CART_TABLE = "tbl_cart";

    private static final String PRIMARY_KEY = "_id";

    private static final String TITLE = "title";
    private static final String LOGO = "logo";
    private static final String MAKER_ID = "maker_id";
    private static final String VEHICLE_TYPE = "vehicle_type";
    private static final String MODEL_ID = "model_id";
    private static final String PRODUCT_ID = "product_id";

    private static final String ACTUAL_PRICE = "actual_price";
    private static final String DISCOUNTED_PRICE = "discounted_price";

    private static final String PRODUCT_NAME = "product_name";

    private static final String PRODUCT_IMAGE = "product_image";
    private static final String QUANTITY = "quantity";
    private static final String TOTAL_PRICE = "total_price";
    private static final String TOTAL_QUANTITY = "total_qty";

    private static final String VENDOR_ID = "vendor_id";

    public static final String CREATE_VEHICLE_MAKE =
            " CREATE TABLE IF NOT EXISTS " + TBL_VEHICLE_MAKE + "(" +PRIMARY_KEY+" INTEGER PRIMARY KEY, "+ TITLE + " TEXT, " + VEHICLE_TYPE + " TEXT, " + LOGO + " TEXT) ";

    public static final String CREATE_MODEL_TABLE =
            " CREATE TABLE IF NOT EXISTS " + TBL_VEHICLE_MODEL + "(" +PRIMARY_KEY+" INTEGER PRIMARY KEY, "+ MAKER_ID + " TEXT, "+ VEHICLE_TYPE + " TEXT, " + TITLE + " TEXT) ";

    public static final String CREATE_VARIANT_TABLE  =
            " CREATE TABLE IF NOT EXISTS " + TBL_VEHICLE_VARIANT + "(" +PRIMARY_KEY+" INTEGER PRIMARY KEY, "+ MODEL_ID + " TEXT, "+ VEHICLE_TYPE + " TEXT, " + TITLE + " TEXT) ";

    public static final String CREATE_CART_TABLE =
            " CREATE TABLE IF NOT EXISTS " + CART_TABLE + "(" +PRIMARY_KEY+" INTEGER PRIMARY KEY, "
                    + PRODUCT_ID + " INTEGER , "+ PRODUCT_NAME + " TEXT , "+ PRODUCT_IMAGE + " TEXT , "+ VENDOR_ID + " TEXT , "+ ACTUAL_PRICE + " TEXT , "+ DISCOUNTED_PRICE + " TEXT , "+ TOTAL_PRICE + " TEXT , "+ QUANTITY + " INTEGER) ";





    private SQLiteDatabase db = null;
    private Context context = null;
    private DataBaseHandler dataBaseHandler = null;

    public GeneralTables(Context context){
        this.context = context;
        this.dataBaseHandler = new DataBaseHandler(context, ConstantUtils.DB_NAME, null, ConstantUtils.DB_VERSION);
        this.dataBaseHandler.getReadableDatabase();

    }


    public void addToCart(ProductModel itemsModel, int qty){
        String total_price_string = "";
        String item_id = itemsModel.getProduct_id();

        String discounted_price = itemsModel.getDiscounted_price();
        Double price =null;
        if (discounted_price !=null) {
            price = Double.parseDouble(discounted_price);
        }


        Double total_price = price*qty;

        if (total_price != null) {
            total_price_string = String.valueOf(total_price);
        }


        if (checkItemFromCart(item_id,itemsModel.getVendor_id()) == 0){

            insert_item_to_cart(itemsModel,qty,total_price_string);

        }else {

            update_item_cart(itemsModel,qty,total_price_string);

        }

    }

    public int getCartItemQty(String itemId, String vendor_id){

        int qty = 0;

        String selectQuery = " SELECT " + QUANTITY + " FROM " + CART_TABLE + " WHERE " + PRODUCT_ID + " = "+itemId+" AND "+VENDOR_ID+" ="+vendor_id;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                qty = cursor.getInt(cursor.getColumnIndex(QUANTITY));

            }
        }
        db.close();
        return qty;
    }

    public ArrayList<ProductModel> getCartItems(){
        ArrayList<ProductModel> productModels = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CART_TABLE+ " WHERE "+QUANTITY +" !=0", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ProductModel productModel = new ProductModel();
                    productModel.setProduct_id(cursor.getString(cursor.getColumnIndex(PRODUCT_ID)));
                    productModel.setTitle(cursor.getString(cursor.getColumnIndex(PRODUCT_NAME)));
                    productModel.setImage(cursor.getString(cursor.getColumnIndex(PRODUCT_IMAGE)));
                    productModel.setVendor_id(cursor.getString(cursor.getColumnIndex(VENDOR_ID)));
                    productModel.setActual_price(cursor.getString(cursor.getColumnIndex(ACTUAL_PRICE)));
                    productModel.setDiscounted_price(cursor.getString(cursor.getColumnIndex(DISCOUNTED_PRICE)));


                    productModel.setCart_qty(cursor.getInt(cursor.getColumnIndex(QUANTITY)));
                    productModel.setTotal_price(cursor.getString(cursor.getColumnIndex(TOTAL_PRICE)));







                    productModels.add(productModel);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return productModels;

    }

    public CartModel cartData(){

        ArrayList<ProductModel> productModels = new ArrayList<>();

        productModels = getCartItems();

        CartModel cartModel = new CartModel();
        cartModel.setProductModels(productModels);
        cartModel.setSub_total(String.valueOf(getTotalCartItemQty()));
        cartModel.setTotal(String.valueOf(getTotalCartItemPrice()));
        return cartModel;
    }



    public int getTotalCartItemQty(){

        int qty = 0;

        String selectQuery = " SELECT SUM(" + QUANTITY + ") AS "+TOTAL_QUANTITY+"  FROM " + CART_TABLE ;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                qty = cursor.getInt(cursor.getColumnIndex(TOTAL_QUANTITY));

            }
        }
        db.close();
        return qty;
    }

    public Double getTotalCartItemPrice(){

        Double price = 0.0;

        String selectQuery = " SELECT SUM(" + TOTAL_PRICE + ") AS "+TOTAL_PRICE+"  FROM " + CART_TABLE ;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {


                price = Double.valueOf(cursor.getInt(cursor.getColumnIndex(TOTAL_PRICE)));

            }
        }
        db.close();
        return price;
    }

    public int deleteItemFromCart(String item_id, String vendor_id){

        db = dataBaseHandler.getWritableDatabase();

        int status = db.delete(CART_TABLE,PRODUCT_ID +" =? AND "+VENDOR_ID +" =?", new String[]{item_id,vendor_id});


        return status;

    }




    private void insert_item_to_cart(ProductModel itemsModel, int qty, String total_price){

        db = dataBaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, itemsModel.getProduct_id());

        contentValues.put(VENDOR_ID, itemsModel.getVendor_id());
        contentValues.put(PRODUCT_NAME, itemsModel.getTitle());
        contentValues.put(ACTUAL_PRICE, itemsModel.getActual_price());
        contentValues.put(DISCOUNTED_PRICE, itemsModel.getDiscounted_price());
        contentValues.put(PRODUCT_IMAGE, itemsModel.getImage());
        contentValues.put(QUANTITY, String.valueOf(qty));
        contentValues.put(TOTAL_PRICE, total_price);
        db.insert(CART_TABLE, null, contentValues);
        db.close();

    }


    private void update_item_cart(ProductModel itemsModel, int qty, String total_price){

        db = dataBaseHandler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACTUAL_PRICE, itemsModel.getActual_price());
        contentValues.put(DISCOUNTED_PRICE, itemsModel.getDiscounted_price());
        contentValues.put(PRODUCT_IMAGE, itemsModel.getImage());
        contentValues.put(QUANTITY, String.valueOf(qty));
        contentValues.put(TOTAL_PRICE, total_price);
        db.update(CART_TABLE, contentValues, PRODUCT_ID + "=" + itemsModel.getProduct_id()+" AND "+VENDOR_ID+ "= "+itemsModel.getVendor_id(), null);

        db.close();


    }


    public int checkItemFromCart(String itemId, String vendor_id) {
        int count = 0;

        String selectQuery = " SELECT " + PRODUCT_ID + " FROM " + CART_TABLE + " WHERE " + PRODUCT_ID + " = "+itemId+ " AND "+VENDOR_ID +" ="+vendor_id;

        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            count = cursor.getCount();
        }
        db.close();
        return count;
    }




    public void insertMake(ArrayList<MakeModel> arrayList){


        clearMakeTable();

        if (arrayList != null){

            if (arrayList.size() > 0){


                for (int i =0; i < arrayList.size();i++){

                    MakeModel makeModel = arrayList.get(i);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PRIMARY_KEY,makeModel.getMaker_id());
                    contentValues.put(TITLE,makeModel.getTitle());
                    contentValues.put(LOGO,makeModel.getLogo());
                    contentValues.put(VEHICLE_TYPE,makeModel.getVehicle_type());


                    db = dataBaseHandler.getWritableDatabase();
                    db.insert(TBL_VEHICLE_MAKE,null,contentValues);
                    db.close();


                }


            }
        }


    }

    public void insertModel(ArrayList<ModelModel> arrayList){


        clearModelTable();

        if (arrayList != null){

            if (arrayList.size() > 0){


                for (int i =0; i < arrayList.size();i++){

                    ModelModel makeModel = arrayList.get(i);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PRIMARY_KEY,makeModel.getModel_id());
                    contentValues.put(TITLE,makeModel.getTitle());
                    contentValues.put(VEHICLE_TYPE,makeModel.getVehicle_type());
                    contentValues.put(MAKER_ID,makeModel.getMaker_id());

                    db = dataBaseHandler.getWritableDatabase();
                    db.insert(TBL_VEHICLE_MODEL,null,contentValues);
                    db.close();
                }
            }
        }
    }



    public void insertVariant(ArrayList<VariantModel> arrayList){


        clearVariantTable();

        if (arrayList != null){

            if (arrayList.size() > 0){


                for (int i =0; i < arrayList.size();i++){

                    VariantModel variantModel = arrayList.get(i);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PRIMARY_KEY,variantModel.getVariant_id());
                    contentValues.put(TITLE,variantModel.getTitle());
                    contentValues.put(MODEL_ID,variantModel.getModel_id());
                    contentValues.put(VEHICLE_TYPE,variantModel.getVehicle_type());

                    db = dataBaseHandler.getWritableDatabase();
                    db.insert(TBL_VEHICLE_VARIANT,null,contentValues);
                    db.close();


                }


            }
        }


    }


    public void clearTables(){

        db = dataBaseHandler.getWritableDatabase();
        db.delete(TBL_VEHICLE_MAKE,null,null);
        db.delete(TBL_VEHICLE_MODEL,null,null);
        db.delete(TBL_VEHICLE_VARIANT,null,null);

        db.close();

    }


    public void clearMakeTable(){

        db = dataBaseHandler.getWritableDatabase();
        db.delete(TBL_VEHICLE_MAKE,null,null);

        db.close();

    }

    public void clearModelTable(){

        db = dataBaseHandler.getWritableDatabase();
        db.delete(TBL_VEHICLE_MODEL,null,null);

        db.close();

    }

    public void clearVariantTable(){

        db = dataBaseHandler.getWritableDatabase();
        db.delete(TBL_VEHICLE_VARIANT,null,null);

        db.close();

    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<MakeModel> getMakersList(int type){

        String query = " SELECT DISTINCT * FROM " + TBL_VEHICLE_MAKE+ " WHERE "+VEHICLE_TYPE+" ="+type+" OR "+VEHICLE_TYPE+" = 3";
        ArrayList<MakeModel> arrayList = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ColumnIndexCache cache = new ColumnIndexCache();
                do {


                    MakeModel makeModel = new MakeModel();
                    makeModel.setMaker_id(cursor.getString(cache.getColumnIndex(cursor,
                            PRIMARY_KEY)));
                    makeModel.setTitle(cursor.getString(cache.getColumnIndex(cursor,
                            TITLE)));
                    makeModel.setVehicle_type(cursor.getString(cache.getColumnIndex(cursor,
                            VEHICLE_TYPE)));

                    arrayList.add(makeModel);


                } while (cursor.moveToNext());

                cache.clear();
            }
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<ModelModel> getModels(int type){
        String query = " SELECT DISTINCT * FROM " + TBL_VEHICLE_MODEL+ " WHERE "+VEHICLE_TYPE+" ="+type+" OR "+VEHICLE_TYPE+" = 3";
        ArrayList<ModelModel> arrayList = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ColumnIndexCache cache = new ColumnIndexCache();
                do {
                    ModelModel modelModel = new ModelModel();
                    modelModel.setModel_id(cursor.getString(cache.getColumnIndex(cursor,
                            PRIMARY_KEY)));
                    modelModel.setTitle(cursor.getString(cache.getColumnIndex(cursor,
                            TITLE)));
                    modelModel.setMaker_id(cursor.getString(cache.getColumnIndex(cursor,
                            MAKER_ID)));
                    arrayList.add(modelModel);
                } while (cursor.moveToNext());
                cache.clear();
            }
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<ModelModel> getModels(String make_id, int type){
        String query = "SELECT DISTINCT * FROM " + TBL_VEHICLE_MODEL+ " WHERE "+VEHICLE_TYPE+" ="+type + " AND "+MAKER_ID+" ="+make_id;
//        String query = "SELECT DISTINCT * FROM " + TBL_VEHICLE_MODEL+ " WHERE "+VEHICLE_TYPE+" ="+type + " OR "+VEHICLE_TYPE+" = 3" + " AND "+MAKER_ID+" ="+make_id;
//        if (!make_id.contentEquals("0"))
//         query = " AND "+MAKER_ID+" ="+make_id;

        ArrayList<ModelModel> arrayList = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ColumnIndexCache cache = new ColumnIndexCache();
                do {
                    ModelModel modelModel = new ModelModel();

//                    Log.d("ModelTitle",cursor.getString(cache.getColumnIndex(cursor,TITLE)));

                    modelModel.setModel_id(cursor.getString(cache.getColumnIndex(cursor,
                            PRIMARY_KEY)));
                    modelModel.setTitle(cursor.getString(cache.getColumnIndex(cursor,
                            TITLE)));
                    modelModel.setMaker_id(cursor.getString(cache.getColumnIndex(cursor,
                            MAKER_ID)));
                    arrayList.add(modelModel);
                } while (cursor.moveToNext());
                cache.clear();
            }
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<VariantModel> getVariants(int type, String model_id){

//        String query = " SELECT DISTINCT * FROM " + TBL_VEHICLE_VARIANT+ " WHERE "+VEHICLE_TYPE+" ="+type+" OR "+VEHICLE_TYPE+" = 3";
        String query = " SELECT DISTINCT * FROM "+ TBL_VEHICLE_VARIANT + " WHERE  " + MODEL_ID + " = " + model_id ;

        ArrayList<VariantModel> arrayList = new ArrayList<>();
        db = dataBaseHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ColumnIndexCache cache = new ColumnIndexCache();
                do {
                    VariantModel variantModel = new VariantModel();
                    variantModel.setVariant_id(cursor.getString(cache.getColumnIndex(cursor,
                            PRIMARY_KEY)));
                    variantModel.setTitle(cursor.getString(cache.getColumnIndex(cursor,
                            TITLE)));
                    arrayList.add(variantModel);
                } while (cursor.moveToNext());

                cache.clear();
            }
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public int clearCart(){

        db = dataBaseHandler.getWritableDatabase();

        int status = db.delete(CART_TABLE,null, null);


        return status;

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public class ColumnIndexCache {
        private ArrayMap<String, Integer> mMap = new ArrayMap<>();
        public int getColumnIndex(Cursor cursor, String columnName) {
            if (!mMap.containsKey(columnName))
                mMap.put(columnName, cursor.getColumnIndex(columnName));
            return mMap.get(columnName);
        }
        public void clear() {
            mMap.clear();
        }
    }
}
