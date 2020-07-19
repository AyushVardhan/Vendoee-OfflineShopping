<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');

	$con = mysqli_connect(HOST,USER,PASS,DB);

	$id = $_POST['id'];

	$sql = "SELECT * FROM sellers where id = $id";

	$res = mysqli_query($con,$sql);

	$result = array();

	while($row = mysqli_fetch_array($res)){
		array_push($result,
		array(
		'name'=>$row[1],
		'bname'=>$row[2],
		'email'=>$row[3],
		'description'=>$row[6],
		'image'=>$row[9],
		'sopen'=>$row[12],
		'sclose'=>$row[13]
		));
	}

	echo json_encode(array("result"=>$result));

mysqli_close($con);

?>