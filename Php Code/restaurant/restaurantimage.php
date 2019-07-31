<?php
session_start();
include("./config.php");

$response = array();
//echo "CHeck user";

// echo $_POST['fname'];

if(isset($_POST['uid']) && isset($_POST['image']) ){
     
    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));
    
    $image = mysqli_real_escape_string($conn,htmlspecialchars($_POST['image']));

    $date = date('Y-m-d H:i:s');

    $query_upload="UPDATE `tbl_restaurants` SET `image_path`='$image',`modified_at`='$date' WHERE `uid`='$uid'";
    
    if (mysqli_query($conn, $query_upload)) {

        array_push($response,
            array('success'=>1,
                'message'=>"Profile picture update Successful.",
                'uid'=> $uid
                ));
        
        } else {
            //echo "Error updating record: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Failed to update profile picture.",
                    'uid'=> $uid,
                    "Error"=>"Error updating record: " . mysqli_error($conn)
                    ));
        }

}

echo json_encode(array('response' => $response));  

//mysqli_close($conn);  