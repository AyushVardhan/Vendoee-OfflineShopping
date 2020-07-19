<?php

    $image = $_POST['image'];
 
    $response = array();
 
    $decodedImage = base64_decode("$image");
 
    $testData = base64_encode("$decodedImage");

    $return = file_put_contents("/var/www/html/images/"."Ayush2".".JPG", $decodedImage);
 
    if($return !== false){
        $response['success'] = 1;
        $response['message'] = "Image Uploaded Successfully";
    }else{
        $response['success'] = 0;
        $response['message'] = "Image Uploaded Failed";
    }
 
    $response['message'] = $testData ;
    echo $response['success'];
?>
