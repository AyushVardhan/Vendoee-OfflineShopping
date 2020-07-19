<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');

	$con = mysqli_connect(HOST,USER,PASS,DB);

	$id = $_POST['id'];
	$sql = "SELECT phone_no FROM seller_contacts where seller_id = $id";

	$res = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result,
		array(
		'contact'=>$row[0]
		));
	}

	echo json_encode(array("result"=>$result));

mysqli_close($con);

?>