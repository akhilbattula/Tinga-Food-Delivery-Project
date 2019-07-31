<?php
session_start();
include("./config.php");

$response = array();

//echo $_POST['uid'];
//$uid='UlvPmGsFCmN7T0YDNGA6HAd58XF3';

if(isset($_POST['uid'])){
//if($uid != null){
   
    $uid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['uid']));

    $query = "SELECT * FROM tbl_users WHERE `uid`='$uid'";
    $res = mysqli_query($conn,$query);
    //echo mysqli_num_rows($res);
    if(mysqli_num_rows($res) > 0) {
        while($row = mysqli_fetch_assoc($res)) {
            if($row['fname']!=null || $row['lname']!=null || $row['phone_number']!=null || $row['mobile_verified']!=null || $row['email']!=null){

                $date = date('Y-m-d H:i:s');

                $sql_log = "INSERT INTO `tbl_users_log` (`uid`, `login_at`) VALUES ('$uid','$date')";
                    if ($conn->query($sql_log) === TRUE) {
                        $last_id = mysqli_insert_id($conn);
                        //echo $last_id;
                        array_push($response,
                        array('success'=>1,
                            'message'=>"User exists with all the details",
                            'uid'=> $uid,
                            'login_id'=>$last_id,
                            'fname'=>$row['fname'],
                             'lname'=>$row['lname'],
                             'phone_number'=>$row['phone_number'],
                             'email'=>$row['email'],
                             'mobile_verified'=>$row['mobile_verified']
                            ));
                    }else{
                        $log_status="Failed to log data.";                
                        $error = $sql_log . "<br>" . $conn->error;
                        //echo $error;
                    }
            }else{
                $date = date('Y-m-d H:i:s');
                
                $sql_log = "INSERT INTO `tbl_users_log` (`uid`, `login_at`) VALUES ('$uid','$date')";
                    if ($conn->query($sql_log) === TRUE) {
                        $last_id = mysqli_insert_id($conn);
                        //echo $last_id;
                        array_push($response,
                           array('success'=>2,
                               'message'=>"User exists with no details",
                               'uid'=> $uid,
                               'login_id'=>$last_id
                               
                               ));
                    }else{
                        $log_status="Failed to log data.";                
                        $error = $sql_log . "<br>" . $conn->error;
                        echo $error;
                    }
            }
        }
    } else {
       
         array_push($response,
                    array('success'=>0,
                        'message'=>"Signup",
                        'uid'=> $uid,
                    
        
                        ));
    }
}
echo json_encode(array('response' => $response));        