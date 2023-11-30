<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="myappApp.dept.home.createOrEditLabel" data-cy="DeptCreateUpdateHeading">Create or edit a Dept</h2>
        <div>
          <div class="form-group" v-if="dept.id">
            <label for="id">Translation missing for global.field.id</label>
            <input type="text" class="form-control" id="id" name="id" v-model="dept.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dept-deptName">Dept Name</label>
            <input
              type="text"
              class="form-control"
              name="deptName"
              id="dept-deptName"
              data-cy="deptName"
              :class="{ valid: !v$.deptName.$invalid, invalid: v$.deptName.$invalid }"
              v-model="v$.deptName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" for="dept-deptAddress">Dept Address</label>
            <input
              type="text"
              class="form-control"
              name="deptAddress"
              id="dept-deptAddress"
              data-cy="deptAddress"
              :class="{ valid: !v$.deptAddress.$invalid, invalid: v$.deptAddress.$invalid }"
              v-model="v$.deptAddress.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Translation missing for entity.action.cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Translation missing for entity.action.save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./dept-update.component.ts"></script>
