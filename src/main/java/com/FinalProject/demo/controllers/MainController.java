package com.FinalProject.demo.controllers;
import com.FinalProject.demo.Models.*;
import com.mashape.unirest.http.HttpResponse;
import org.springframework.core.env.Environment;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class MainController {


    DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date date = new Date();
    String currentDate = formatter.format(date);
    //Formatted to match API
    DateFormat apiFormat = new SimpleDateFormat("M/d/yy");
    DateFormat htmlFormat = new SimpleDateFormat("yyyy-MM-dd");
    String currentDateWHTMLFormat = htmlFormat.format(date);

    @Autowired
    CountryRepo countryRepo;
    @Autowired
    UsersRepo usersRepo;

    Users currentUser = new Users();

    @RequestMapping(value = "/loginPage")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("loginPage");
        return mv;
    }

    //Login model and view
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord) {

        Users userLogin = usersRepo.findByUserName(userName);
        String userPassword = userLogin.getPassword();
        //password verification
        if (passWord.equals(userPassword)) {
            ModelAndView mv = new ModelAndView("redirect:/");
            //Current user privilege set
            currentUser.setAdmin(userLogin.getAdmin());
            return mv;
        } else {
            String incorrect = "Username or Password is incorrect";
            ModelAndView mv = new ModelAndView("redirect:/");
            mv.addObject("incorrectLogin", incorrect);
            return mv;
        }
    }
    //Index modelandview
    @RequestMapping(value = "/")
    public ModelAndView allCountries() {
        ModelAndView mv = new ModelAndView("index");

        //Adds line chart
        CanvasjsChartData chartCasesDataObject = new CanvasjsChartData();
        List<List<Map<Object, Object>>> canvasjsCasesDataList = chartCasesDataObject.getCanvasjsCasesDataList();
        mv.addObject("dataPointsList", canvasjsCasesDataList);
        String countries = getAllCountries();
        //Adds API data to html table
        try {
            JSONArray json = new JSONArray(countries);
            Country[] allCountries = new Country[50];
            for (int i = 0; i < 50; i++) {
                JSONObject countryList = json.getJSONObject(i);
                String country = countryList.getString("country");
                allCountries[i] = new Country();
                allCountries[i].setCountryName(country);
                allCountries[i].setDate(currentDate);
                allCountries[i].setTotalCases((countryList.getInt("cases")));
                allCountries[i].setNewCases((countryList.getInt("todayCases")));
                allCountries[i].setTotalDeaths((countryList.getInt("deaths")));
                allCountries[i].setRecovered((countryList.getInt("recovered")));

            }
            mv.addObject("countryNames", allCountries);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return mv;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ModelAndView getCountry(@RequestParam("country") String countryIso, @RequestParam("date") String datePicked) throws ParseException {
        ModelAndView mv = new ModelAndView("viewCountry");


        Date dateFound = formatDateMethodView(datePicked);

        //Date for fetching historical data
        String dateFoundApi = apiFormat.format(dateFound);
        String dateFoundHtml = htmlFormat.format(dateFound);
        String dateForView = formatter.format(dateFound);


        Country selectedCountry = new Country();
        //Fetch data from most recent API
        if (dateForView.equals(currentDate)) {
            try {
                String country = getCountry(countryIso);
                JSONObject json = new JSONObject(country);
                selectedCountry.setCountryName(json.get("country").toString());
                selectedCountry.setDate(currentDate);
                selectedCountry.setTotalCases(json.getInt("cases"));
                selectedCountry.setTotalDeaths(json.getInt("deaths"));
                selectedCountry.setRecovered(json.getInt("recovered"));
                mv.addObject("country", selectedCountry);

            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
            //Fetch data from historical api
            else {
                try {
                    String country = getCountryByISO(countryIso);
                    JSONObject json = new JSONObject(country);
                    selectedCountry.setCountryName(json.get("country").toString());
                    selectedCountry.setTotalCases(json.getJSONObject("timeline").getJSONObject("cases").getInt(dateFoundApi));
                    selectedCountry.setDate(dateForView);
                    selectedCountry.setTotalDeaths(json.getJSONObject("timeline").getJSONObject("deaths").getInt(dateFoundApi));
                    selectedCountry.setRecovered(json.getJSONObject("timeline").getJSONObject("recovered").getInt(dateFoundApi));
                    mv.addObject("country", selectedCountry);
                }
                catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        return mv;

    }

    @RequestMapping(value = "/save/{countryName}", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable("countryName") String name, @RequestParam("date") String datePicked) throws ParseException {
        ModelAndView mv = new ModelAndView("redirect:/");



            Date apiDateFormat = formatDateMethodSave(datePicked);
            String dateFoundApi = apiFormat.format(apiDateFormat);
            String selectedDate = formatter.format(apiDateFormat);

        //Fetch data from most recent API
        if (selectedDate.equals(currentDate)) {
            try {
                String country = getCountry(name);
                JSONObject json = new JSONObject(country);
                Country countryToSave = new Country();
                countryToSave.setId(UUID.randomUUID().toString());
                countryToSave.setCountryName(json.get("country").toString());
                countryToSave.setDate(currentDate);
                countryToSave.setTotalCases(json.getInt("cases"));
                countryToSave.setTotalDeaths(json.getInt("deaths"));
                countryToSave.setRecovered(json.getInt("recovered"));
                countryRepo.save(countryToSave);
            } catch (Exception ex) {
                System.out.println(ex.toString());

            }
        } else {
            //Fetch data from historical API
            try {
                String country = getCountryByISO(name);
                JSONObject json = new JSONObject(country);
                Country countryToSave = new Country();
                countryToSave.setId(UUID.randomUUID().toString());
                countryToSave.setCountryName(json.get("country").toString());
                countryToSave.setDate(selectedDate);
                countryToSave.setTotalCases(json.getJSONObject("timeline").getJSONObject("cases").getInt(dateFoundApi));
                countryToSave.setTotalDeaths(json.getJSONObject("timeline").getJSONObject("deaths").getInt(dateFoundApi));
                countryToSave.setRecovered(json.getJSONObject("timeline").getJSONObject("recovered").getInt(dateFoundApi));
                countryRepo.save(countryToSave);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }

        return mv;
    }
    //Snapshot modelandview
    @RequestMapping(value = "/viewSnapshots")
    public ModelAndView viewSnapshots() {
        ModelAndView mv = new ModelAndView("viewSnapshots");
        mv.addObject("countryList", countryRepo.findAll());
        return mv;
    }
    //Edit db snapshots modelandview
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable("id") String id) {
        ModelAndView mv;
        try {
            //Check admin privileges Try/catch to prevent null pointer exception when no user has logged in
            if (currentUser.getAdmin()) {
                mv = new ModelAndView("edit");
                Optional<Country> countryRecord = countryRepo.findById(id);
                Country country = countryRecord.get();
                mv.addObject("country", country);
                return mv;
            }

        } catch (NullPointerException ex) {
            mv = new ModelAndView("accessDenied");
            return mv;
        }
        ModelAndView mv2 = new ModelAndView("viewSnapshots");
        return mv2;
    }

    //Submit changes from edit page
    @RequestMapping(value = "/submitChanges", method = RequestMethod.POST)
    public ModelAndView changes(@RequestParam("id") String id, @RequestParam("totalCases") String totalCases, @RequestParam("totalDeaths") String totalDeaths,
                                @RequestParam("newCases") String newCases) {
        ModelAndView mv = new ModelAndView("redirect:/viewSnapshots");
        Optional<Country> countryRecord = countryRepo.findById(id);
        Country country = countryRecord.get();

        country.setTotalCases(Integer.parseInt(totalCases));
        country.setTotalDeaths(Integer.parseInt(totalDeaths));
        country.setNewCases(Integer.parseInt(newCases));
        countryRepo.save(country);
        return mv;
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") String id) {
        ModelAndView mv = new ModelAndView("redirect:/viewSnapshots");
        countryRepo.deleteById(id);
        return mv;
    }


    @RequestMapping(value = "viewChart/{id}", method = RequestMethod.GET)
    public ModelAndView viewChart(@PathVariable("id") String id) {
        ModelAndView mv = new ModelAndView("viewChart");

        Optional<Country> countryRecord = countryRepo.findById(id);

        Country c1 = countryRecord.get();

        int countryDeaths = c1.getTotalDeaths();
        int countryRecovered = c1.getRecovered();
        int countryCases = c1.getTotalCases();
        //Add data to pie chart
        CanvasjsPieChart chartDataObject = new CanvasjsPieChart();
        List<List<Map<Object, Object>>> canvasjsDataList = chartDataObject.getCanvasjsDataList();
        chartDataObject.map = new HashMap<Object,Object>();
        chartDataObject.map.put("label", "Cases");
        chartDataObject.map.put("y", countryCases);
        chartDataObject.dataPoints1.add(chartDataObject.map);
        chartDataObject.map = new HashMap<Object,Object>();
        chartDataObject.map.put("label", "Deaths");
        chartDataObject.map.put("y", countryDeaths);
        chartDataObject.dataPoints1.add(chartDataObject.map);
        chartDataObject.map = new HashMap<Object,Object>();
        chartDataObject.map.put("label", "Recovered");
        chartDataObject.map.put("y", countryRecovered);
        chartDataObject.dataPoints1.add(chartDataObject.map);


        mv.addObject("dataPointsList", canvasjsDataList);


        return mv;
    }

    private String getCountryByISO(String ISO) {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/historical/" + ISO + "?lastdays=356");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        } catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }
    }

    private String getCountry(String ISO) {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/countries/" + ISO + "?strict=true");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        } catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }
    }

    private String getAllCountries() {
        try {
            URL urlForGetRequest = new URL("https://disease.sh/v3/covid-19/countries?yesterday=true&sort=cases");
            HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                return response.toString();
            } else {
                return "Unexpected HTTP response";
            }
        } catch (Exception ex) {
            return "Exception: " + ex.getMessage();
        }


    }
    private Date formatDateMethodView(String datePicked) throws ParseException {
        try {
            Date dateFound = htmlFormat.parse(datePicked);
            String foundDate = htmlFormat.format(dateFound);
            System.out.println(foundDate);

            return dateFound;
        } catch (Exception ex) {
          return date;
        }
    }
    private Date formatDateMethodSave(String datePicked) throws ParseException {
        try {
            Date dateFound = formatter.parse(datePicked);
            String foundDate = apiFormat.format(dateFound);
            System.out.println(foundDate);
            return dateFound;
        } catch (Exception ex) {
            System.out.println(ex);
            return date;
        }

    }


}
