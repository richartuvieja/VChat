package com.kuikos.vchat.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.androidchat.R;
import com.google.firebase.database.Query;
import com.kuikos.vchat.model.Chat;
import com.kuikos.vchat.model.Usuario;
import com.squareup.picasso.Picasso;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private Usuario usuario;
    private String mUsername;
    private LayoutInflater mInflater;
    private Activity activity;


    public ChatListAdapter(Query ref, Activity activity, int item_chat_others, String mUsername, Usuario usuario) {
        super(ref, Chat.class, item_chat_others,  activity,mUsername);
        this.usuario=usuario;
        mInflater = activity.getLayoutInflater();
        this.mUsername = mUsername;
        this.activity=activity;
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     *
     *
     *
     *
     */

    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
        String author = chat.getAuthor_username();
        String genero = chat.getGender();

        Log.e("On", "Genero: " + genero);
        TextView authorText = (TextView) view.findViewById(R.id.item_autor);
        TextView messageText = (TextView) view.findViewById(R.id.item_mensaje);
        ImageView imagen = (ImageView) view.findViewById(R.id.item_imagen);
        CardView card = (CardView) view.findViewById(R.id.card_item);
        //Selecciona el meme --------------------------------------------
        int meme = -1;
        switch (chat.getMessage()){
            case ":calmarno:": meme = R.drawable.meme_calmarno; break;
            case ":abajo:": meme = R.drawable.meme_abajo; break;
            case ":gordo:": meme = R.drawable.meme_gordocentral; break;
            case ":linx:": meme = R.drawable.meme_linx; break;
            case ":soap:": meme = R.drawable.meme_soap; break;
            case ":comandante:": meme = R.drawable.meme_comandante; break;
            case ":guacho:": meme = R.drawable.meme_guacho; break;
            case ":troll:": meme = R.drawable.meme_troll; break;
            case ":tomatela:": meme = R.drawable.meme_tomatela; break;
            case ":judio:": meme = R.drawable.meme_judio; break;
        }

        authorText.setText(author);

        if (meme!=-1){ //Si el mensaje es un meme
            messageText.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            imagen.setImageResource(meme);
            messageText.setText("");

        }else if (chat.getMessage().trim().startsWith("@:")){ //Si el mensaje es una imagen
            messageText.setVisibility(View.GONE);
            imagen.setVisibility(View.VISIBLE);
            String url = chat.getMessage().replace("@:","").replace(" ","");
            if (url.isEmpty()) url="empty";

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            int iderror= R.drawable.ic_image_broken_grey600_36dp;
            if (author != null && author.equals(mUsername))
                iderror= R.drawable.ic_image_broken_white_36dp;

            Picasso.with(activity).
                    load(url).
                    resize((int)(Math.min(width,height)*0.80),0).
                    placeholder(R.drawable.progress_animation).
                    error(iderror).
                    into(imagen);

            messageText.setText("");
        }
        else{
            imagen.setVisibility(View.GONE);
            messageText.setVisibility(View.VISIBLE);
            messageText.setText(chat.getMessage());
        }
        // Fin slecciona el meme ------------------------

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) card.getLayoutParams();

        if (author != null && author.equals(mUsername)) {

            authorText.setGravity(Gravity.RIGHT);
            authorText.setTextColor(Color.WHITE);
            messageText.setGravity(Gravity.RIGHT);
            messageText.setTextColor(Color.WHITE);
            params.gravity = Gravity.RIGHT;
            card.setLayoutParams(params);
            card.setCardBackgroundColor(view.getResources().getColor(R.color.color_primary));

        } else {

            authorText.setTextColor(Color.GRAY);

            if (genero.equals("M"))
                authorText.setTextColor(view.getResources().getColor(R.color.hombre));
            else
                authorText.setTextColor(view.getResources().getColor(R.color.mujer));

            if (author!=null && author.equals("Richar_tuvieja"))
                authorText.setTextColor(view.getResources().getColor(R.color.color_accent));

            authorText.setGravity(Gravity.LEFT);
            messageText.setGravity(Gravity.LEFT);
            messageText.setTextColor(Color.GRAY);
            params.gravity = Gravity.LEFT;
            card.setLayoutParams(params);
            card.setCardBackgroundColor(Color.WHITE);
        }
    }


}
