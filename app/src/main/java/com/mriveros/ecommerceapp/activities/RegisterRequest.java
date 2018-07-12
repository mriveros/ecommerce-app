package com.mriveros.ecommerceapp.activities;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://www.smarthub.design/ecommerce/register.php";
    private Map<String, String> params;

    public RegisterRequest(String name,String lastname, String birtdate,String username, String password,String address, String city, String neigborhood, String phone, String mail, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("lastname", lastname);
        params.put("birtdate", birtdate);
        params.put("username", username);
        params.put("password", password);
        params.put("address", address);
        params.put("city", city);
        params.put("neigboorhood", neigborhood);
        params.put("phone", phone);
        params.put("mail", mail);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
