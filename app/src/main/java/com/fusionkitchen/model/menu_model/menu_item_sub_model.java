package com.fusionkitchen.model.menu_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class menu_item_sub_model {

    @SerializedName("searchcategory")
    private List<menu_item_sub_model.searchcategory> searchcategory;


    @SerializedName("categoryall")
    private List<menu_item_sub_model.categoryall> categoryall;

    public List<menu_item_sub_model.categoryall> getCategoryall() {
        return categoryall;
    }

    public void setCategoryall(List<menu_item_sub_model.categoryall> categoryall) {
        this.categoryall = categoryall;
    }

    public List<menu_item_sub_model.searchcategory> getSearchcategory() {
        return searchcategory;
    }

    public void setSearchcategory(List<menu_item_sub_model.searchcategory> searchcategory) {
        this.searchcategory = searchcategory;
    }

    public class categoryall {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("subcat")
        private List<menu_item_sub_model.categoryall.subcat> subcat;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<menu_item_sub_model.categoryall.subcat> getSubcat() {
            return subcat;
        }

        public void setSubcat(List<menu_item_sub_model.categoryall.subcat> subcat) {
            this.subcat = subcat;
        }

        public class subcat {

            @SerializedName("name")
            private String name;


            @SerializedName("description")
            private String description;

            @SerializedName("items")
            private List<menu_item_sub_model.categoryall.subcat.items> items;


            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<menu_item_sub_model.categoryall.subcat.items> getItems() {
                return items;
            }

            public void setItems(List<menu_item_sub_model.categoryall.subcat.items> items) {
                this.items = items;
            }

            public class items {
                @SerializedName("name")
                private String name;

                @SerializedName("description")
                private String description;

                @SerializedName("price")
                private String price;

                @SerializedName("image")
                private String image;

                @SerializedName("id")
                private String id;

                @SerializedName("sid")
                private String sid;

                @SerializedName("item_type")
                private String itemtype;

                @SerializedName("available_time")
                private String available_time;


                @SerializedName("best_seller")
                private String best_seller;

                @SerializedName("must_try")
                private String must_try;


                public String getBestseller() {
                    return best_seller;
                }

                public void setBestseller(String best_seller) {
                    this.best_seller = best_seller;
                }


                public String getMusttry() {
                    return must_try;
                }

                public void setMusttry(String must_try) {
                    this.must_try = must_try;
                }


                public String getAvailableTime() {
                    return available_time;
                }

                public void setAvailableTime(String available_time) {
                    this.available_time = available_time;
                }



                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }


                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }


                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSid() {
                    return sid;
                }

                public void setSid(String sid) {
                    this.sid = sid;
                }

                public String getItemtype() {
                    return itemtype;
                }

                public void setItemtype(String itemtype) {
                    this.itemtype = itemtype;
                }


            }


        }
    }

    public class searchcategory {


        @SerializedName("categoryid")
        private String categoryid;

        @SerializedName("name")
        private String name;

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class ordermodecs {

        @SerializedName("delivery")
        private String delivery;

        @SerializedName("collection")
        private String collection;


        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }
    }
}
