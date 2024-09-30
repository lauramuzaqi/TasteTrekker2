package com.example.tastetrekkerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tastetrekkerapp.Database.DatabaseHelper;
import com.example.tastetrekkerapp.Models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public FavoritesManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addFavorite(String userId, Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, recipe.id);
        values.put(DatabaseHelper.COLUMN_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_TITLE, recipe.title);
        values.put(DatabaseHelper.COLUMN_SERVINGS, recipe.servings);
        values.put(DatabaseHelper.COLUMN_TIME, recipe.readyInMinutes);
        values.put(DatabaseHelper.COLUMN_IMAGE, recipe.image);
        database.insert(DatabaseHelper.TABLE_FAVORITES, null, values);
    }

    public boolean isFavorite(String userId, int recipeId) {
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ? AND " + DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId), String.valueOf(recipeId) };
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, null, selection, selectionArgs, null, null, null);

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void removeFavorite(String userId, int recipeId) {
        String whereClause = DatabaseHelper.COLUMN_USER_ID + " = ? AND " + DatabaseHelper.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(userId), String.valueOf(recipeId) };
        database.delete(DatabaseHelper.TABLE_FAVORITES, whereClause, whereArgs);
    }

    public List<Recipe> getAllFavorites(String userId) {
        List<Recipe> favoriteRecipes = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, null, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            try {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int titleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
                int servingsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SERVINGS);
                int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);
                int imageIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE);

                while (cursor.moveToNext()) {
                    Recipe recipe = new Recipe();
                    if (idIndex != -1) recipe.id = cursor.getInt(idIndex);
                    if (titleIndex != -1) recipe.title = cursor.getString(titleIndex);
                    if (servingsIndex != -1) recipe.servings = cursor.getInt(servingsIndex);
                    if (timeIndex != -1) recipe.readyInMinutes = cursor.getInt(timeIndex);
                    if (imageIndex != -1) recipe.image = cursor.getString(imageIndex);
                    favoriteRecipes.add(recipe);
                }
            } finally {
                cursor.close();
            }
        }

        return favoriteRecipes;
    }

    public void close() {
        dbHelper.close();
    }
}