<?php

$path = '/var/www/html/images/Ayush2.JPG';
$type = pathinfo($path, PATHINFO_EXTENSION);
$data = file_get_contents($path);
$base64 = base64_encode($data);
  
    
    echo $base64;
?>