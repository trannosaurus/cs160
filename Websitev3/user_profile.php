<?php require("includes/users_connect.php");
    if (empty($_SESSION['username'])) {
        header("Location: index.php");
        die("Redirecting to index.php");
    }
?>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
        <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet">
        <link href="fancyBox/source/jquery.fancybox.css" rel="stylesheet">
        <link href="DataTables-1.10.0/media/css/jquery.dataTables.css" rel="stylesheet">
        <link href="css/style.css" type="text/css" rel="stylesheet">
        <title>Educademy User Profile</title>
    </head>
    <body>
        <?php include('includes/header.php'); ?>
        <div class="container">
            <div class="row well">
                <div class="col-md-2">
                    <ul class="nav nav-pills nav-stacked well">
                        <li class="active"><a href="user_profile.php"><i class="fa fa-home"></i> Home</a></li>
                        <li><a href="logout.php"><i class="fa fa-sign-out"></i> Logout</a></li>
                    </ul>
                </div>
                <div class="col-md-10">
                    <div class="panel">
                        <img class="img-circle" src="images/user.jpg" alt="avatar" style="margin-top:50px;width:120px;margin-left:50px;margin-bottom:-60px;">
                        <div style="position:absolute;padding-left:200px;font-size:30px;"><small><?php echo $_SESSION['username'] ?></small></div>
                    </div>

                    <br><br><br>
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active"><a href="#rated" data-toggle="tab"><i class="fa fa-star-o"></i> Rated Courses</a></li>
                        <li><a href="#wish" data-toggle="tab"><i class="fa fa-list-ol"></i> Wish List</a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="rated">
                            <table class="table table-striped table-hover table-responsive">
                                <thead>
                                    <th></th>
                                    <th>Rating</th>
                                    <th>Course Name</th>
                                </thead>
                                <tbody>
                                    <?php
                                    foreach ($_SESSION['user_data']['ratedCourses'] as $courseId => $courseInfo) {
                                        echo '<tr courseId="' . $courseId . '">';
                                        echo '<td><input type="checkbox"></td>';
                                        echo '<td class="raty" value="' . $courseInfo['rating'] . '"></td>';
                                        echo '<td><a href="' . $courseInfo['link'] . '" target="_blank"><b>' . $courseInfo['title'] . '</b></a></td>';
                                        echo '</tr>';
                                    }
                                    ?>
                                <tbody>
                            </table>
                            <div id="a1" class="collapse out well">MORE DETAILED COURSE STUFF HERE</div>
                            <br />
                            <button onclick="deleteRatedCourses()" class="btn btn-primary btn-xs"><i class="fa fa-check-square-o"></i> Delete Checked Items</button>
                        </div>

                        <div class="tab-pane" id="wish">
                            <table class="table table-striped table-hover table-responsive">
                                <thead>
                                    <th></th>
                                    <th>Course Name</th>
                                </thead>
                                <tbody>
                                    <?php
                                    foreach ($_SESSION['user_data']['wishlist'] as $courseId => $courseTitle) {
                                        echo '<tr courseId="' . $courseId . '">';
                                        echo '<td><input type="checkbox"></td>';
                                        echo '<td><b>' . $courseTitle . '</b></a></td>';
                                        echo '</tr>';
                                    }
                                    ?>
                                <tbody>
                            </table>
                            <button onclick="deleteWishList()" class="btn btn-primary btn-xs"><i class="fa fa-check-square-o"></i> Delete Checked Items</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <?php include('includes/footer.php'); ?>
        <!-- JS placed at the end of the document so the page loads faster -->
        <script src="js/jquery-2.1.0.min.js"></script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="js/jquery.raty.js"></script>
        <script src="DataTables-1.10.0/media/js/jquery.dataTables.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="fancyBox/source/jquery.fancybox.pack.js"></script>
        <script src="fancyBox/source/helpers/jquery.fancybox-media.js"></script>
        <script src="js/application.js"></script>
    </body>
</html>
