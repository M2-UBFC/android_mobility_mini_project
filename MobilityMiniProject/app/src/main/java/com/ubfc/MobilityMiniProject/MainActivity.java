package com.ubfc.MobilityMiniProject;

import android.content.DialogInterface;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import java.util.Arrays;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    double[][]  ODMatrix = new double[10][10];
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ODMatrix[0][2] = 1.7;
        ODMatrix[0][4] = 1.2;
        ODMatrix[0][9] = 2.5;
        ODMatrix[1][2] = 1.75;
        ODMatrix[1][8] = .950;
        ODMatrix[3][4] = .950;
        ODMatrix[3][7] = 1.5;
        ODMatrix[4][6] = 1.75;
        ODMatrix[5][7] = 3.8;
        ODMatrix[5][8] = 2.8;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.mapView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setVerticalScrollBarEnabled(false);
        myWebView.setHorizontalScrollBarEnabled(false);
        myWebView.loadUrl("file:///android_asset/map.html");

    }

    public void resetMap(View view){
        myWebView.loadUrl("file:///android_asset/map.html");
    }
    public void getID(View view){
        myWebView.loadUrl("file:///android_asset/map.html");
        Button button = (Button) findViewById(R.id.GoButton);
        Spinner spin = (Spinner) findViewById(R.id.start1);
        button.setText(String.valueOf(spin.getSelectedItemPosition()));
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(String.valueOf(findReachables(3)[1]));
    }

    //returns the id of the place closest on average to everybody
    public int computeDestination(final View view){
        Spinner spin1 = (Spinner) findViewById(R.id.start1);
        Spinner spin2 = (Spinner) findViewById(R.id.start2);
        Spinner spin3 = (Spinner) findViewById(R.id.start3);

        int start1 = spin1.getSelectedItemPosition();
        int start2 = spin2.getSelectedItemPosition();
        int start3 = spin3.getSelectedItemPosition();

        double bestTotalDistance = 0;
        int bestIndex = 0;

        for(int i = 0; i<10; i++){
                double currentDistance = findBestItinerary(i, start1).getDistance() + findBestItinerary(i, start2).getDistance() + findBestItinerary(i, start3).getDistance();
                if ((currentDistance < bestTotalDistance || bestTotalDistance == 0)){
                    bestTotalDistance = currentDistance;
                    bestIndex = i;
                }
                Log.d("DESTINATION DISTANCE", String.valueOf(currentDistance));

        }
        final Spinner destSpinner = (Spinner) findViewById(R.id.endSpinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Best destination for meeting: " + destSpinner.getItemAtPosition(bestIndex));
        builder.setCancelable(true);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        destSpinner.setSelection(bestIndex);


        myWebView.loadUrl("javascript:updateItineraries(" + arrToStr(findBestItinerary(start1, bestIndex).getPath()) + ", " + arrToStr(findBestItinerary(start2, bestIndex).getPath()) + ", "+ arrToStr(findBestItinerary(start3, bestIndex).getPath()) +", "+ bestIndex +");");
        Log.d("URL REQUEST : ", "javascript:updateItineraries(" + arrToStr(findBestItinerary(start1, bestIndex).getPath()) + ", " + arrToStr(findBestItinerary(start2, bestIndex).getPath()) + ", "+ arrToStr(findBestItinerary(start3, bestIndex).getPath()) +", "+ bestIndex +")");
        return bestIndex;
    }

    public Itinerary findBestItinerary(int start, int dest){
        ArrayList<ArrayList<Node>> possiblePaths = new ArrayList<>();
        if (ODMatrix[start][dest] != 0 || ODMatrix[dest][start] != 0){
            return new Itinerary(ODMatrix[dest][start]+ODMatrix[start][dest], new int[] {start, dest}); //transitif, on retourne le chemin direct
        }else{
            //build tree
            Node root = new Node(start, 0, null);
            ArrayList<Node> queue = new ArrayList<Node>();
            ArrayList<Node> newAdditions = new ArrayList<Node>();

            for(int i: findReachables(start)){
                queue.add(root.insert(i, ODMatrix[start][i]+ODMatrix[i][start]));
            }
            int counter = 0;
            Log.d("QUEUE", queue.toString());
            while(queue.size() > 0 && counter < 10){
                for(Node n:queue){
                    for(int newChild : findReachables(n.value)) {
                        Node newNode = n.insert(newChild, ODMatrix[n.value][newChild]+ODMatrix[newChild][n.value]);
                        if (newNode != null && newNode.value != dest) newAdditions.add(0,newNode); //on insère les nouveaux enfants
                        else if(newNode != null) possiblePaths.add(newNode.getAncestors(null));
                    }
                }
                queue = new ArrayList<>(newAdditions);
                newAdditions = new ArrayList<>();
                counter++;
                //Log.d("QUEUE", String.valueOf(queue.get(0).value));
            }
            int bestIndex = 0;
            double bestDistance = .0;
            for (ArrayList<Node> path: possiblePaths){
                double distance = .0;
                Log.d("BEGIN PATH", "______________");
                for (Node n : path){
                    distance += n.distance;
                    Log.d("PATH", String.valueOf(n.value) + " cost " + String.valueOf(n.distance));
                }
                Log.d("TOTAL DIST", String.valueOf(distance));
                if (distance < bestDistance || bestDistance == 0) {
                    bestDistance = distance;
                    bestIndex = possiblePaths.indexOf(path);
                    }
                }
            Log.d("BEST DIST", String.valueOf(bestDistance));

            ArrayList<Node> bestPath = possiblePaths.get(bestIndex);
            int[] result = new int[bestPath.size()];
            for(int k=0; k<bestPath.size(); k++){
                result[k] = bestPath.get(k).value;
            }
            return new Itinerary(bestDistance, result);
            }
        }



    public int[] findReachables(int id){
        int[] res = {};
        for (int i=0; i<ODMatrix.length; i++) {
            if (ODMatrix[id][i] != 0 || ODMatrix[i][id] != 0 && i!=id){
                res = Arrays.copyOf(res, res.length + 1);
                res[res.length - 1] = i;
            }
        }
        return res;
    }

    //return array of steps with start and end (ex : [0,4,5] for a 0-> itinerary)
    public int[] ComputeItinerary(View view){

        Spinner spin1 = (Spinner) findViewById(R.id.start1);
        Spinner spin2 = (Spinner) findViewById(R.id.start2);
        Spinner spin3 = (Spinner) findViewById(R.id.start3);

        Spinner destSpin = (Spinner) findViewById(R.id.endSpinner);

        String start1 = String.valueOf(spin1.getSelectedItemPosition());
        String start2 = String.valueOf(spin2.getSelectedItemPosition());
        String start3 = String.valueOf(spin3.getSelectedItemPosition());
        String dest = String.valueOf(destSpin.getSelectedItemPosition());

        int start = 0;
        int[] result = {start};
        myWebView.loadUrl("javascript:updateItineraries(" + start1 + ", " + start2 + ", "+ start3 +", "+ dest +")");

        return result;
    }

    public String arrToStr(int[] arr){
        String output = "[";
        for (int i:arr){
            output = output.concat(String.valueOf(i));
            if (i != arr[arr.length-1]) output = output.concat(",");
        }
        output = output.concat("]");
        return output;
    }
}
