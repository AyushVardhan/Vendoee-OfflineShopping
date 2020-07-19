<?php

$phoneNo = $_POST['phoneNo'];                      
      			   

//	$APIKey = "9e5cb388-08ab-11e7-9462-00163ef91450";
	$APIKey = "fb223f2d-0cde-11e7-9462-00163ef91450";
	
	//API URL
    	$url = "https://2factor.in/API/V1/".$APIKey."/SMS/".$phoneNo."/AUTOGEN";
 
// init the resource
    $ch = curl_init();
    curl_setopt_array($ch, array(
        CURLOPT_URL => $url,
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST => true,
        CURLOPT_POSTFIELDS => $postData
            //,CURLOPT_FOLLOWLOCATION => true
    ));
 
 
    //Ignore SSL certificate verification
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
 
 
    //get response
    $output = curl_exec($ch);
 
    //Print error if any
    if (curl_errno($ch)) {
        echo 'error:' . curl_error($ch);
    }
 
    curl_close($ch);
    
    echo $output;
    	

?>