<?php
session_start();
include("./config.php");

$response = array();

//echo $_POST['uid'];


if(isset($_POST['mobile'])){

    $mobile = mysqli_real_escape_string($conn,htmlspecialchars($_POST['mobile']));

    $query = "SELECT `uid`,`mobile_verified` FROM tbl_users WHERE `phone_number`='$mobile'";
    $res = mysqli_query($conn,$query);
    //echo mysqli_num_rows($res);
    if(mysqli_num_rows($res) > 0) {
        $row = mysqli_fetch_assoc($res);
         array_push($response,
                    array('success'=>1,
                        'message'=>"Mobile number already exists in Tinga database",
                        'uid'=>$row['uid'],
                        'verification'=>$row['mobile_verified']
                        ));
    } else {       
        array_push($response,
                    array('success'=>0,
                        'message'=>"Mobile number is not exists in Tinga database",
                        'uid'=>"",
                        'verification'=>0
                        ));
    }
}
echo json_encode(array('response' => $response));  

